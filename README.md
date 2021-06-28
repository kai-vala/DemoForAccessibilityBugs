# Demo for a bug in CollapsingToolbarLayout with keyboard scrolling

## The following should occur consistently with Android 11 on simulator:

1. Fresh start the app

2. Scroll down the view with keyboard only

3. Notice that the entire view is not made visible while CollapsingToolbarLayout is expanded


## Workaround for the issue:

We implement AppBarLayoutKeyboardHandler, which allows us to collapse/expand the layout ourselves
when the user is navigating with keyboard.

How it works by default:
When focus is inside the ViewGroup with AppBarLayout.ScrollingViewBehavior

1. User presses up twice -> We expand the layout

2. User presses down twice -> We collapse the layout

Collapsing the layout while the user is navigating down ensures that the entire view is always visible, even for edge cases where the view might not be 'scrollable', but could still be cut off. 