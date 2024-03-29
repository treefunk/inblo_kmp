#ifdef __OBJC__
#import <UIKit/UIKit.h>
#else
#ifndef FOUNDATION_EXPORT
#if defined(__cplusplus)
#define FOUNDATION_EXPORT extern "C"
#else
#define FOUNDATION_EXPORT extern
#endif
#endif
#endif

#import "CAMediaTimingFunction+MDCAnimationTiming.h"
#import "MaterialAnimationTiming.h"
#import "UIView+MDCTimingFunction.h"
#import "MaterialAvailability.h"
#import "MDCAvailability.h"
#import "MaterialRipple.h"
#import "MDCRippleTouchController.h"
#import "MDCRippleTouchControllerDelegate.h"
#import "MDCRippleView.h"
#import "MDCRippleViewDelegate.h"
#import "MDCStatefulRippleView.h"
#import "MaterialTabs+TabBarView.h"
#import "MDCTabBarItem.h"
#import "MDCTabBarItemCustomViewing.h"
#import "MDCTabBarView.h"
#import "MDCTabBarViewCustomViewable.h"
#import "MDCTabBarViewDelegate.h"
#import "MDCTabBarViewIndicatorAttributes.h"
#import "MDCTabBarViewIndicatorContext.h"
#import "MDCTabBarViewIndicatorTemplate.h"
#import "MDCTabBarViewUnderlineIndicatorTemplate.h"
#import "MaterialTextControls+BaseTextFields.h"
#import "MDCBaseTextField.h"
#import "MDCBaseTextFieldDelegate.h"
#import "MaterialTextControls+Enums.h"
#import "MDCTextControlLabelBehavior.h"
#import "MDCTextControlState.h"
#import "MaterialTextControls+OutlinedTextFields.h"
#import "MDCOutlinedTextField.h"
#import "MaterialColor.h"
#import "UIColor+MaterialBlending.h"
#import "UIColor+MaterialDynamic.h"
#import "MaterialMath.h"
#import "MDCMath.h"
#import "MaterialTextControlsPrivate+BaseStyle.h"
#import "MDCTextControlStyleBase.h"
#import "MDCTextControlVerticalPositioningReferenceBase.h"
#import "MaterialTextControlsPrivate+OutlinedStyle.h"
#import "MDCTextControlStyleOutlined.h"
#import "MDCTextControlVerticalPositioningReferenceOutlined.h"
#import "MaterialTextControlsPrivate+Shared.h"
#import "MDCTextControl.h"
#import "MDCTextControlAssistiveLabelDrawPriority.h"
#import "MDCTextControlAssistiveLabelView.h"
#import "MDCTextControlAssistiveLabelViewLayout.h"
#import "MDCTextControlColorViewModel.h"
#import "MDCTextControlGradientManager.h"
#import "MDCTextControlHorizontalPositioning.h"
#import "MDCTextControlHorizontalPositioningReference.h"
#import "MDCTextControlLabelAnimation.h"
#import "MDCTextControlLabelSupport.h"
#import "MDCTextControlPlaceholderSupport.h"
#import "MDCTextControlSideViewSupport.h"
#import "MDCTextControlVerticalPositioningReference.h"
#import "UIBezierPath+MDCTextControlStyle.h"
#import "MaterialTextControlsPrivate+TextFields.h"
#import "MDCBaseTextFieldLayout.h"
#import "MDCTextControlTextField.h"
#import "MDCTextControlTextFieldSideViewAlignment.h"

FOUNDATION_EXPORT double MaterialComponentsVersionNumber;
FOUNDATION_EXPORT const unsigned char MaterialComponentsVersionString[];

