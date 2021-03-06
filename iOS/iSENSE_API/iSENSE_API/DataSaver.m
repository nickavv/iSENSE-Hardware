//
//  DataSaver.m
//  iSENSE API
//
//  Created by Jeremy Poulin on 4/26/13.
//  Copyright 2013 iSENSE Development Team. All rights reserved.
//  Engaging Computing Lab, Advisor: Fred Martin
//

#import "DataSaver.h"
#import "DataFieldManager.h"

@implementation DataSaver

@synthesize count, dataQueue, managedObjectContext;

-(id)initWithContext:(NSManagedObjectContext *)context {
    self = [super init];
    if (self) {
        count = 0;
        dataQueue = [[NSMutableDictionary alloc] init];
        managedObjectContext = context;
    }
    return self;
}

// add a DataSet to the queue
-(void)addDataSet:(DataSet *)dataSet {
    
    DataSet *ds = [NSEntityDescription insertNewObjectForEntityForName:@"DataSet" inManagedObjectContext:managedObjectContext];
    
    ds.name = dataSet.name;
    ds.dataDescription = dataSet.dataDescription;
    ds.data = dataSet.data;
    ds.picturePaths = dataSet.picturePaths;
    ds.eid = dataSet.eid;
    ds.city = dataSet.city;
    ds.country = dataSet.country;
    ds.uploadable = dataSet.uploadable;
    ds.address = dataSet.address;
    ds.sid = dataSet.sid;
        
    // Commit the changes
    NSError *error = nil;
    if (![managedObjectContext save:&error]) {
        // Handle the error.
        NSLog(@"%@", error);
    }
    
    int newKey = arc4random();
    [dataQueue enqueue:dataSet withKey:newKey];
    count++;
}

-(void)addDataSetFromCoreData:(DataSet *)dataSet {
    int newKey = arc4random();
    [dataQueue enqueue:dataSet withKey:newKey];
    count++;

}

// if key is nil, call dequeue otherwise dequeue with the given key
-(id)removeDataSet:(NSNumber *)key {
    count--;
    DataSet *tmp;
    if (key == nil) {
        tmp = [dataQueue dequeue];
    } else {
        tmp = [dataQueue removeFromQueueWithKey:key];
    }
    
    [managedObjectContext deleteObject:tmp];

    // Commit the changes
    NSError *error = nil;
    if (![managedObjectContext save:&error]) {
        // Handle the error.
        NSLog(@"Save failed: %@", error);
    }
    
    return tmp;
}

-(id)getDataSet {
    NSNumber *firstKey = [dataQueue.allKeys objectAtIndex:0];
    return [dataQueue objectForKey:firstKey];
}

// if key is nil, call dequeue otherwise dequeue with the given key
-(void)removeAllDataSets {
    
    for (int i = 0; i < dataQueue.count; i++) {
        NSNumber *tmp = [dataQueue.allKeys objectAtIndex:i];
        NSLog(@"deleting %@", tmp);
        [self removeDataSet:tmp];
    }
    
    [dataQueue removeAllObjects];
    count = 0;

}

-(void)editDataSetWithKey:(NSNumber *)key {
    DataSet *dataSet = [dataQueue removeFromQueueWithKey:key];
    [dataQueue enqueue:dataSet withKey:key];
}

-(bool)upload:(NSString *)parentName {
    iSENSE *isenseAPI = [iSENSE getInstance];
    if (![isenseAPI isLoggedIn]) {
        
        NSLog(@"Not logged in.");
        return false;
    }
    
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    int dataSetsToUpload = 0;
    int dataSetsFailed = 0;
    
    NSMutableArray *dataSetsToBeRemoved = [[NSMutableArray alloc] init];
    DataSet *currentDS;
    
    for (NSNumber *currentKey in dataQueue.allKeys) {
        
        // get the next dataset
        currentDS = [dataQueue objectForKey:currentKey];
        
        // prevent uploading datasets from other sources (e.g. manual vs automatic)
        if (![currentDS.parentName isEqualToString:parentName]) continue;
        
        // prevent trying to upload with an invalid experiment
        if (currentDS.eid.intValue <= 0) continue;
        
        // check if the session is uploadable
        if (currentDS.uploadable.boolValue) {
            
            dataSetsToUpload++;
            
            // create a session
            if (currentDS.sid.intValue == -1) {
                NSNumber *sessionID = [isenseAPI createSession:currentDS.name withDescription:currentDS.dataDescription Street:currentDS.address City:currentDS.city Country:currentDS.country toExperiment:currentDS.eid];
                if (sessionID.intValue == -1) {
                    continue;
                } else {
                    currentDS.sid = sessionID;
                }
            }
            
            // organize data if no initial experiment was found
            if (currentDS.hasInitialExp.boolValue == FALSE) {
                DataFieldManager *dfm = [[DataFieldManager alloc] init];
                currentDS.data = [dfm reOrderData:currentDS.data forExperimentID:currentDS.eid.intValue];
            }
            
            // upload to iSENSE
            if (((NSArray *)currentDS.data).count) {
                NSError *error = nil;
                NSData *dataJSON = [NSJSONSerialization dataWithJSONObject:currentDS.data options:0 error:&error];
                if (error != nil) {
                    NSLog(@"%@", error);
                    return false;
                }
                
                if (![isenseAPI putSessionData:dataJSON forSession:currentDS.sid inExperiment:currentDS.eid]) {
                    dataSetsFailed++;
                    continue;
                }
            }
            
            // upload pictures to iSENSE
            if (((NSArray *)currentDS.picturePaths).count) {
                NSArray *pictures = (NSArray *) currentDS.picturePaths;
                NSMutableArray *newPicturePaths = [NSMutableArray alloc];
                bool failedAtLeastOnce = false;
                
                // loop through all the images and try to upload them
                for (int i = 0; i < pictures.count; i++) {
                    
                    // track the images that fail to upload
                    if (![isenseAPI upload:pictures[i] toExperiment:currentDS.eid forSession:currentDS.sid withName:currentDS.name andDescription:currentDS.dataDescription]) {
                        dataSetsFailed++;
                        failedAtLeastOnce = true;
                        [newPicturePaths addObject:pictures[i]];
                        continue;
                    }

                }
                
                // add back the images that need to be uploaded
                if (failedAtLeastOnce) {
                    currentDS.picturePaths = newPicturePaths;
                    continue;
                }
            }
            
        [dataSetsToBeRemoved addObject:currentKey];
            
        } else {
            continue;
        }
        
        
    }
    
    [self removeDataSets:dataSetsToBeRemoved];
    
    if (dataSetsToUpload > 0)
        if (dataSetsFailed > 0)
            [prefs setInteger:DATA_UPLOAD_FAILED forKey:@"key_data_uploaded"];
        else
            [prefs setInteger:DATA_UPLOAD_SUCCESS forKey:@"key_data_uploaded"];
    else
        [prefs setInteger:DATA_NONE_UPLOADED forKey:@"key_data_uploaded"];
    
    
    return true;
}

-(void)removeDataSets:(NSArray *)keys {
    for(NSNumber *key in keys) {
        [self removeDataSet:key];
    }
}

@end