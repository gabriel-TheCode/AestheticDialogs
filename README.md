# Aesthetic Dialogs for Android 📱
[![platform](https://img.shields.io/badge/platform-Android-yellow.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=plastic)](https://android-arsenal.com/api?level=14)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://jitpack.io/v/gabriel-TheCode/AestheticDialogs.svg)](https://jitpack.io/#gabriel-TheCode/AestheticDialogs)

📱Android Library to implement *custom*, 😍*beautiful*, 🎨*awesome*  Dialog in android apps easily.

<a href="https://play.google.com/store/apps/details?id=com.thecode.sample">
    <img alt="Get it on Google Play"
        height="80"
        src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" />
</a>

# Table of Contents:
> - [ Introduction ](#introduction)
> - [ Types of Dialog ](#types)
> - [ Implementation ](#implementation)
>    - [ Prerequisite ](#prerequisite)
>    - [ Create Dialog ](#createDialog)
> - [ Demo ](#demo)
> - [ Contribute ](#contribute)
> - [ Credits ](#credits)

<a name="introduction"></a>
## Introduction
**AestheticDialogs** is a library that provides beautiful and custom Dialog inspired by [Laravel Notify](https://github.com/mckenziearts/laravel-notify)

<a name="types"></a>
## Types of Dialog
**AestheticDialog** At this moment, library provides seven types of dialog i.e.

<table style="width:100%">
  <tr>
    <th>1. Flash Dialog</th>
    <th>2. Connectify Dialog</th>
    <th>3. Toaster Dialog</th>
  </tr>
  <tr>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/blob/master/AestheticDialogs/flash.gif"/></td>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/blob/master/AestheticDialogs/connectify.gif"/></td>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/blob/master/AestheticDialogs/toaster.gif"/></td>
  </tr>
  <tr>
    <th>4. Emotion Dialog</th>
    <th>5. Drake Dialog</th>
    <th>6. Emoji Dialog</th>
  </tr>
  <tr>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/blob/master/AestheticDialogs/emotion.gif"/></td>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/blob/master/AestheticDialogs/drake.gif"/></td>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/blob/master/AestheticDialogs/emoji.gif"/></td>
  </tr>
  
   <tr>
    <th>7. Rainbow Dialog</th>
    <th colspan="2">LET's USE aesthetic Dialog !</th>
  </tr>

   <tr>
    <th><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/blob/master/AestheticDialogs/emoji.gif"/></th>
    <th colspan="2"></th>
  </tr>
</table>

<a name="dark"></a>
## Dark Theme
**AestheticDialog** Also provide Dark Theme mode for some dialogs i.e.

<table style="width:100%">
  <tr>
    <th><b>1. Connectify Dialog<b></b></th>
    <th>2. Toaster Dialog</th>
  </tr>
  <tr>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/blob/master/AestheticDialogs/connectify-dark.png"/></td>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/blob/master/AestheticDialogs/toaster-dark.png"/></td>
  </tr>
</table>

<a name="implementation"></a>
## Implementation
Implementation of Material Dialog library is so easy. You can check [/app](/app) directory for demo. Let's have look on basic steps of implementation.
<a name="prerequisite"></a>
### Prerequisite
#### i. Gradle

Add it in your root `build.gradle` at the end of repositories:

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

Step 2. Add the dependency

```gradle
dependencies {
	...
	implementation 'com.github.gabriel-TheCode:AestheticDialogs:1.0.0'
}
```

<a name="createDialog"></a>
### Create Dialog

Flash:

``` java
                AestheticDialog.showFlashDialog(this, "Your dialog Title", "Your message", AestheticDialog.SUCCESS);
                AestheticDialog.showFlashDialog(this, "Your dialog Title", "Your message", AestheticDialog.ERROR);
```

Connectify:

``` java
                AestheticDialog.showConnectify(this,"Your message", AestheticDialog.SUCCESS);
                AestheticDialog.showConnectify(this, "Your message", AestheticDialog.ERROR);

                /// Dark Mode
                AestheticDialog.showConnectifyDark(this,"Your message",AestheticDialog.SUCCESS);
                AestheticDialog.showConnectifyDark(this, "Your message", AestheticDialog.ERROR);
```

Toaster:

``` java
                AestheticDialog.showToaster(this, "Your dialog Title", "Your message", AestheticDialog.ERROR);
                AestheticDialog.showToaster(this, "Your dialog Title", "Your message", AestheticDialog.SUCCESS);
                AestheticDialog.showToaster(this, "Your dialog Title", "Your message", AestheticDialog.WARNING);
                AestheticDialog.showToaster(this, "Your dialog Title", "Your message", AestheticDialog.INFO);

                /// Dark Mode 
                AestheticDialog.showToasterDark(this, "Your dialog Title", "Your message", AestheticDialog.ERROR);
                AestheticDialog.showToasterDark(this, "Your dialog Title", "Your message", AestheticDialog.SUCCESS);
                AestheticDialog.showToasterDark(this, "Your dialog Title", "Your message", AestheticDialog.WARNING);
                AestheticDialog.showToasterDark(this, "Your dialog Title", "Your message", AestheticDialog.INFO);
```

Drake :

``` java
                AestheticDialog.showDrake(this, AestheticDialog.SUCCESS);
                AestheticDialog.showDrake(this, AestheticDialog.ERROR);
```

Emoji :

``` java
                AestheticDialog.showEmoji(this,"Your dialog Title", "Your message", AestheticDialog.SUCCESS);
                AestheticDialog.showEmoji(this, "Your dialog Title", "Your message", AestheticDialog.ERROR);
```

Emotion :

``` java
                AestheticDialog.showEmotion(this,"Your dialog Title", "Your message", AestheticDialog.SUCCESS);
                AestheticDialog.showEmotion(this, "Your dialog Title", "Your message", AestheticDialog.ERROR);
```

<a name="demo"></a>
## Demo
You can download the demo app on [PlayStore](https://play.google.com/store/apps/details?id=com.thecode.sample)

<a name="contribute"></a>
## Contribute
Let's develop with collaborations. We would love to have contributions by raising issues and opening PRs. Filing an issue before PR is must.
See [Contributing Guidelines](CONTRIBUTING.md).

<a name="credits"></a>
## Credits
This library is built using following open-source libraries.
- [Material Components for Android](https://github.com/material-components/material-components-android)

## License
Project is published under the Apache 2.0 license. Feel free to clone and modify repo as you want, but don't forget to add reference to authors :)
