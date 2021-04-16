# Demo for Accessibility bugs in Android

NOTE 1: Enabled Talkback on your device before following the steps. 

NOTE 2: This works slightly differently depending on Android version and sometimes it even works correctly with Talkback enabled.

---

## The following should occur consistently with Android 11 on simulator:

- Select one of the tabs -> corresponding 'Edit text view' becomes unselectable

- For example, select Tab2 -> Edit text 2 cannot be selected

### Detailed steps to repro:

1. Fresh start the app

2. Select Tab 2.

3. Try to select 'Edit text 2' view, notice it cannot be clicked (swipe navigation with Talkback will also bypass it)

4. Tap the button to 'Toggle other text boxes' after selecting one of the tabs

5. The 'Edit text 2' view can now be selected.

Again, these issues only occur with Talkback and the Databinding for the Tab.

---

Example of the issue in practice:

![Example of view not selectable](/images/animation.gif?raw=true)

---

## Brief overview of the code

Relevant code is in the `MainViewModel`, the `MainActivity` only sets up view model / binding

In `activity_main.xml` the TabLayout will update `MainViewModel.tabIndex` via:

- `app:tabIndex="@={vm.tabIndex}"`

When the tab is changed this will trigger the `@BindingAdapter` for `tabIndexSet()`

Then `tabIndexListener` will trigger `TabLayout.OnTabSelectedListener.onTabSelected` to update the `@InverseBindingAdapter`, which will trigger the visibility changes in the layout via:

- `app:visible="@{vm.tabIndex == 0}"`, editText1, visible when Tab 1 is selected

- `app:visible="@{vm.tabIndex == 1}"`, editText2, visible when Tab 2 is selected

For some reason after this visibility change the edit text is not 'selectable' when the layout change is done with this binding code.

When applying simpler logic (a button with onClick that changes ViewModel value), the text boxes are selectable immediately after they become visible (referred to as 'Edit text other 1' and 'Edit text other 2').
