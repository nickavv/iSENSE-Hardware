//
//  ISenseSearch.m
//  iOS Data Collector
//
//  Created by Jeremy Poulin on 2/5/13.
//  Copyright 2013 iSENSE Development Team. All rights reserved.
//  Engaging Computing Lab, Advisor: Fred Martin
//

#import "ISenseSearch.h"

@implementation ISenseSearch

@synthesize query, searchType, buildType, page;

- (id) init {
    self = [super init];
    if (self) {
        query = @"";
        searchType = RECENT;
        buildType = NEW;
        page = 1;
    }
    
    return self;
}

- (id) initWithQuery:(NSString *)q searchType:(SearchType)st page:(int)p andBuildType:(BuildType)bt {
    self = [self init];
    if (self) {
        query = [q retain];
        searchType = st;
        buildType = bt;
        page = p;
    }
    return self;
}

- (NSString *) searchTypeToString {
    switch (searchType) {
        case RECENT:
            return @"Recent";
        case RATING:
            return @"Rating";
        case POPULARITY:
            return @"Popularity";
        case ACTIVITY:
            return @"Activity";
    }
}

@end
