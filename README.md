# Demo for an Accessibility/Talkback bug when transitions are used in Android

NOTE: Enabled Talkback on your device

This is a demo for an issue where the user has enabled 'Heading navigation' and opens a new Fragment.

In the new Fragment the user can no longer navigate via 'Swipe right' and 'Swipe left', Talkback will simply announce 'no next heading'.

---

## The following should occur consistently with Android 11 on simulator:

1. Fresh start the app

2. Enable 'Heading navigation mode' in Talkback if needed

3. Try to navigate via swipe right / swipe left in the initial view, this will work.

4. Click 'Open a new fragment'

5. Try to navigate via swipe right / swipe left, this will no longer work. Talkback will simply announce 'no next heading'.