# AdvancedUINavApp – Activity 07

**Name:** Gerald Mamasalanang  
**Section:** DIT 3-1  
**Activity Title:** Fragments, Navigation, and Adaptive UI  
**Repository Name:** DIT3-1-GeraldMamasalanang-Act07

## Project Description

This project is an Android app called **AdvancedUINavApp** that demonstrates modular UI design using Fragments, Bottom Navigation, Tabbed Layouts, and Adaptive UI for different screen sizes and orientations.

The main features include:

- **Three main sections** accessible via Bottom Navigation (Home, Profile, Settings)
- **Bottom Navigation** using Navigation Component with nav_graph.xml
- **Tabbed Layout** in Profile section using TabLayout + ViewPager2 (Info and Gallery tabs)
- **Adaptive UI** supporting both portrait and landscape orientations
- **Tablet-optimized layouts** using layout-sw600dp resource folder
- **ConstraintLayout** for flexible and responsive positioning

## Project Architecture

```
app/src/main/java/com/example/advanceduinavapp/
├── MainActivity.kt                    # Main activity with BottomNavigationView
├── adapters/
│   └── ProfilePagerAdapter.kt         # ViewPager2 adapter for profile tabs
└── fragments/
    ├── HomeFragment.kt                # Home section fragment
    ├── ProfileFragment.kt             # Profile section with TabLayout
    ├── SettingsFragment.kt            # Settings section fragment
    ├── InfoFragment.kt                # Info tab content (child of Profile)
    └── GalleryFragment.kt             # Gallery tab content (child of Profile)

app/src/main/res/
├── layout/                            # Default layouts for phones
├── layout-sw600dp/                    # Tablet-optimized layouts
├── navigation/
│   └── nav_graph.xml                  # Navigation graph for fragments
└── menu/
    └── bottom_nav_menu.xml            # Bottom navigation menu items
```

## Technologies Used

- **Kotlin** - Primary programming language
- **Navigation Component** - Fragment navigation with nav_graph.xml
- **BottomNavigationView** - Bottom navigation bar for main sections
- **ViewPager2** - Swipeable tab content in Profile section
- **TabLayout** - Tab headers for Info and Gallery tabs
- **TabLayoutMediator** - Connects TabLayout with ViewPager2
- **ConstraintLayout** - Flexible layout positioning
- **Material Design** - Modern UI components and theming

## Setup Instructions

1. Clone this repository:
   ```bash
   git clone git@github.com:mamasalanang-gerald/DIT3-1-GeraldMamasalanang-Act07.git
   ```

2. Open the project in Android Studio

3. Build the project (Build → Make Project)

4. Run on device or emulator

## How to Use

1. **Home Tab** - View the welcome screen with app introduction
2. **Profile Tab** - Swipe between Info and Gallery tabs, or tap tab headers
3. **Settings Tab** - View settings options (Notifications, Privacy, About)
4. **Rotate Device** - UI adapts to landscape orientation
5. **Tablet Mode** - Layouts optimize for larger screens (sw600dp+)

## Reflection

### 1. What did you learn about using fragments and navigation components?

I learned that Fragments provide modular, reusable UI components that can be combined within a single Activity. The Navigation Component simplifies fragment transactions by defining destinations in nav_graph.xml and automatically handling the back stack. Using `setupWithNavController()` seamlessly connects BottomNavigationView with the NavController for intuitive navigation.

### 2. How did you make your UI adaptive to screen size or orientation?

I used ConstraintLayout as the root layout for all fragments, which automatically adjusts view positions based on constraints. For tablets, I created alternative layouts in the `layout-sw600dp` folder with larger text sizes, increased padding, and optimized spacing. The app doesn't lock orientation in AndroidManifest.xml, allowing the system to handle configuration changes naturally.

### 3. What challenges did you face when combining Bottom Navigation and Tabs?

The main challenge was ensuring proper fragment lifecycle management when nesting ViewPager2 inside ProfileFragment. I used `FragmentStateAdapter` with the parent fragment (not activity) to properly scope the child fragments. Another challenge was coordinating the TabLayoutMediator to sync tab selection with ViewPager2 page changes, which required attaching it in `onViewCreated()` after the adapter was set.

## Screenshots

Screenshots should be placed in the `/activity7/` folder:
- Bottom Navigation working between fragments
- Tabs visible and functioning in Profile section
- App layout in both portrait and landscape orientations

## How to Run

```bash
# Clone the repository
git clone git@github.com:mamasalanang-gerald/DIT3-1-GeraldMamasalanang-Act07.git

# Open in Android Studio
# Build → Make Project
# Run on emulator or device
```
