//
//  StepOneSetup.h
//  iOS Data Collector
//
//  Created by Mike Stowell on 06/21/13.
//  Copyright 2013 iSENSE Development Team. All rights reserved.
//  Engaging Computing Lab, Advisor: Fred Martin
//

#import <UIKit/UIKit.h>
#import <iSENSE_API/SensorCompatibility.h>
#import <iSENSE_API/SensorEnums.h>
#import "SensorSelection.h"

#import "Data_CollectorAppDelegate.h"
#import <AVFoundation/AVFoundation.h>
#import <AVFoundation/AVCaptureDevice.h>

@interface StepOneSetup : UIViewController <UITextFieldDelegate> {
    
    iSENSE *iapi;
    int expNumInteger;
    bool sensorsSelected;
    bool displaySensorSelectFromBrowse;
    
}

- (IBAction)rememberMeToggled:(UISwitch *)switcher;
- (IBAction)selectLaterToggled:(UISwitch *)switcher;
- (IBAction)experimentOnClick:(UIButton *)expButton;
- (IBAction)okOnClick:(UIButton *)okButton;

- (BOOL) handleNewQRCode:(NSURL *)url;

@property (nonatomic, retain) IBOutlet UITextField *sessionName;
@property (nonatomic, retain) IBOutlet UITextField *sampleInterval;
@property (nonatomic, retain) IBOutlet UITextField *testLength;
@property (nonatomic, retain) IBOutlet UILabel     *expNumLabel;
@property (nonatomic, retain) IBOutlet UISwitch    *rememberMe;
@property (nonatomic, retain) IBOutlet UIButton    *selectExp;
@property (nonatomic, retain) IBOutlet UISwitch    *selectLater;
@property (nonatomic, retain) IBOutlet UIButton    *ok;

@end