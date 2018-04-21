# HiddenLayout
A Library to that provides hidden options on swiping with built-in animations. 
## What it does
HiddenLayout creates a view using two layouts overlapped over each other - when you slide the upper-layout, the layout underneath is visible. This is very much similar to the slide animations we often see in iOS apps like iMessages. To make it work, all you need to do is provide two separate layouts and provide them to the Custom View which will be explained in a later section. You can refer to the sample layouts given in the library.

## Setup 
Include the dependencies
```groovy
    implementation 'com.psx95:hidden-layout-view:0.1.0'
    implementation 'com.android.support:support-dynamic-animation:27.1.1'
```    
And add the Support for JAVA 8 
```
android {
 ....
  compileOptions {
          sourceCompatibility JavaVersion.VERSION_1_8
          targetCompatibility JavaVersion.VERSION_1_8
      }
 ....
    }
```    
You're all set.

![alt text](https://github.com/psx95/HiddenLayout/tree/master/gifs/fling.gif "normalFling Type Animation")
*HiddenLayout With Fling Animation*
![alt text](https://github.com/psx95/HiddenLayout/tree/master/gifs/spring.gif "dragWithSpring Type Animation")
*HiddenLayout with Spring Animation*

## Available Attributes - XML

* **layout_over (layout reference)** - This is the layout that will overlap the hidden layout and will contain the view that will be visible initially.
* **layout_under (layout reference)** - This is the layout that will be under the layout_over and will not be visible unless an action (Fling or Drag) is made by the user. This layout will contain the View which you want to reveal after the user makes an action.
* **revealPercentageViewRight (float)** - This is float and should be between 0 and 1. Indicates the % of the layout_under you wish to reveal once the user makes the swipe action. **The view will be revealed from right end of the screen. Default value is 0.2 which is 20%** 
* **animationEffect** - Allows user to select the preferred animation for revealing the hidden view. Currently 2 available options - **dragWithSpring (Uses Android's Spring Animation)** and **normalFling (Uses Android's Fling Animation)**
* **scaleHiddenView (boolean)** - This indicates if you want the view to scale if the Drag action is exceeds the revealPercentageViewRight.
* **maxMovementFactorForSpring (float)** - This indicates the maximum drag allowed after the revealPercentageViewRight of layout_under has been revealed. **Default value is 2**. 
```
    If revealPercentageViewRight is set to 0.2(20%) and maxMovementFactorForSpring is set to 2 (200%),
    Then you can drag the over_layout to a maximum of 0.4 (40%)
    - As soon as you stop dragging, it will come back to 20%
```
* **flingFriction (float)** - This indicates the default friction for the Fling type animation. You can read more about Friction in FlingAnimation [here](https://developer.android.com/guide/topics/graphics/fling-animation.html). **Default Value is 0.8**
* **flingFrictionReverse (float)** - This indicates the default friction for the reversefling Animation - which comes into effect if the revealed view is closed using the function - closeRightHiddenView(). **Recommended Value range is between 0.001 to 0.1, default value is 0.001**.

## Available public methods 
```java
HiddenLayoutView hiddenLayoutView;
hiddenLayoutView = findViewById(R.id.hidden_view);
```

* **void closeRightHiddenView()** - Used to close the hidden view if its open. 
```java 
hiddenLayoutView.closeRightHiddenView();
```
* **void changeAnimation(String animationType)** - Used the switch between animation types (DRAG and FLING). The parameters passed to this function can be **ANIMATION_FLING** or **ANIMATION_DRAG_SPRING**. Both of these are static constants described in the library.
```java 
hiddenLayoutView.changeAnimation(ANIMATION_DRAG_SPRING);
```
* **View getInflatedUnderLayout()** - Returns the Inflated View of the layout_under. Helpful in finding individual views on the hidden layout and setting up listeners on them.
* **View getInflatedOverLayout()** - Returns the Inflated View of the layout_over. Helpful in finding individual views on the hidden layout and setting up listeners on them.
* **DynamicAnimation getAnimationInEffect()** - Returns the current selected Animation (either SpringAnimation or FlingAnimation) object. The returned object can be used to customize various animation properties. Various properties of SpringAnimation can be found [here](https://developer.android.com/guide/topics/graphics/spring-animation.html) and FlingAnimation properties can be found [here](https://developer.android.com/guide/topics/graphics/fling-animation.html).
* **DynamicAnimation getReverseAnimationInEffect()** - Does the same thing as the getAnimationInEffect() but returns object for Animation that is used to close the hidden view.
* **void setDampingAndStiffnessForDragWithSpringForward(float damping, float stiffness)** - Used to modify the damping ratio and stiffness of the [Sping animation](https://developer.android.com/guide/topics/graphics/spring-animation.html).
* **void setDampingAndStiffnessForDragWithSpringReverse(float dampning, float stiffness)** - Used to modify the damping ratio and stiffness of the Reverse [Sping animation](https://developer.android.com/guide/topics/graphics/spring-animation.html)(The one used to close the view).

## Event Listeners - 
* **AnimationUpdateListeners.OverLayoutEventListener.onOverLayoutClickReceived(View v)** - Listens for the click events occouring on the over_layout.
```java
    hiddenLayoutView.setOverLayoutEventListener(view -> 
    Toast.makeText(getApplicationContext(),"Pressed Revealed View "+view.getId(),Toast.LENGTH_SHORT).show());
```
* **AnimationUpdateListeners.UnderLayoutEventListener.onUnderLayoutClickReceived(View v)** - Listens for click events occouring on the under_layout.
```java
    hiddenLayoutView.setUnderLayoutEventListener(view -> {
            Toast.makeText(getApplicationContext(),"Pressed View hidden "+view.getId(),Toast.LENGTH_SHORT).show();
            hiddenLayoutView.closeRightHiddenView();
        });
```
* **AnimationUpdateListeners.onMaxSpringPull()** - Called whenever the user drags the layout_over to the maximum allowed.
```java
    hiddenLayoutView.setAnimationUpdateListeners(() -> Toast.makeText(context,"Max Pull event",Toast.LENGTH_SHORT).show());
```    
## Auto close any open views on activity change (Optional) -
```java 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_with_spring);
        hiddenLayoutView = findViewById(R.id.hidden_spring);       
        getLifecycle().addObserver(hiddenLayoutView);
    }
    ....
      @Override
    protected void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(hiddenLayoutView);
    }
```
This will automtically close any open views when the users switches between activities. 

