//
//  SplashAppDelegate.h
//  Splash
//
//  Created by CS Admin on 12/4/12.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SplashAppDelegate : NSObject <UIApplicationDelegate> {
    IBOutlet UIWindow *window;
	IBOutlet UITabBarController *tbc;
	IBOutlet UIImageView *orb;
	IBOutlet UITextView *aboutText;
	IBOutlet UITextView *guideText;
	IBOutlet UIButton *automatic;
	IBOutlet UIButton *manual;
	
}

-(void)rotateImage:(UIImageView *)image duration:(NSTimeInterval)duration 
			 curve:(int)curve degrees:(CGFloat)degrees;

-(NSString *) getString:(NSString *)label;


@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UITabBarController *tbc;
@property (nonatomic, retain) IBOutlet UIImageView *orb;
@property (nonatomic, retain) IBOutlet UITextView *aboutText;
@property (nonatomic, retain) IBOutlet UITextView *guideText;
@property (nonatomic, retain) IBOutlet UIButton *automatic;
@property (nonatomic, retain) IBOutlet UIButton *manual;

@end
