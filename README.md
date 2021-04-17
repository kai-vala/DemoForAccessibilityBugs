# Demo for an Accessibility/Talkback bug when transitions are used in Android

NOTE: Enabled Talkback on your device

This is a demo for an issue where Views are not selectable with Talkback after a transition occurs.

If we remove this line, the views that are made visible will be selectable with Talkback enabled:

`TransitionManager.beginDelayedTransition(it)`

---

## The following should occur consistently with Android 11 on simulator:

1. Fresh start the app

2. Tap the button to perform transition

3. Try to select 'Edit text 2' view, notice it cannot be clicked (swipe navigation with Talkback will also bypass it)

---

Example of the issue in practice (this gif is from a slightly more complex version of the code):

![Example of view not selectable](/images/animation.gif?raw=true)

---

See `MainActivity.kt`, the `MainViewModel` doesn't really hold anything relevant for this example.

