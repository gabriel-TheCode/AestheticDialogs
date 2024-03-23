# Aesthetic Dialogs for Android 📱
[![platform](https://img.shields.io/badge/platform-Android-yellow.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=plastic)](https://android-arsenal.com/api?level=14)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://jitpack.io/v/gabriel-TheCode/AestheticDialogs.svg)](https://jitpack.io/#gabriel-TheCode/AestheticDialogs)
[![Open Source? Yes!](https://badgen.net/badge/Open%20Source%20%3F/Yes%21/blue?icon=github)](https://github.com/Naereen/badges/)
![Downloads](https://jitpack.io/v/gabriel-TheCode/AestheticDialogs/month.svg)


📱 Android Library for 💫*fluid*, 😍*beautiful*, 🎨*custom*  Dialogs.

<a href="https://play.google.com/store/apps/details?id=com.thecode.sample">
    <img alt="Get it on Google Play"
        height="80"
        src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" />
</a>

# Table of Contents:
> - [ Introduction ](#introduction)
> - [ Types of Dialog ](#types)
> - [ Dark Theme ](#dark)
> - [ Implementation ](#implementation)
>    - [ Prerequisite ](#prerequisite)
>    - [ Create Dialog ](#createDialog)
> - [ Demo ](#demo)
> - [ Contribute ](#contribute)
> - [ Credits ](#credits)
> - [ License ](#license)

<a name="introduction"></a>
## Introduction
**AestheticDialogs** is a library that provides beautiful and custom Dialog inspired by [Laravel Notify](https://github.com/mckenziearts/laravel-notify)

<a name="types"></a>
## Types of Dialog
**AestheticDialog** At this moment, library provides eight types of dialog i.e.

<table style="width:100%">
  <tr>
    <th>1. Flash Dialog</th>
    <th>2. Connectify Dialog</th>
    <th>3. Toaster Dialog</th>
  </tr>
  <tr>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/raw/master/AestheticDialogs/flash.gif"/></td>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/raw/master/AestheticDialogs/connectify.gif"/></td>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/raw/master/AestheticDialogs/toaster.gif"/></td>
  </tr>
  <tr>
    <th>4. Emotion Dialog</th>
    <th>5. Drake Dialog</th>
    <th>6. Emoji Dialog</th>
  </tr>
  <tr>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/raw/master/AestheticDialogs/emotion.gif"/></td>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/raw/master/AestheticDialogs/drake.gif"/></td>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/raw/master/AestheticDialogs/emoji.gif"/></td>
  </tr>
  
   <tr>
    <th>7. Rainbow Dialog</th>
    <th>8. Flat Dialog</th>
  </tr>

   <tr>
    <th><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/raw/master/AestheticDialogs/rainbow.png"/></th>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/raw/master/AestheticDialogs/flat.png"/></td>

  </tr>
</table>

<a name="dark"></a>
## Dark Mode
**AestheticDialog** Also provides Dark Theme for some dialogs i.e.

<table style="width:100%">
  <tr>
    <th>1. Connectify Dark Dialog</th>
    <th>2. Toaster Dark Dialog</th>
    <th>3. Emoji Dark Dialog</th>
  </tr>
  <tr>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/raw/master/AestheticDialogs/connectify-dark.png"/></td>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/raw/master/AestheticDialogs/toaster-dark.png"/></td>
     <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/raw/master/AestheticDialogs/emoji-dark.png"/></td>
  </tr>
  <tr>
    <th>4. Flat Dark Dialog</th>
    <th colspan="2">LET's USE aesthetic Dialog !</th>
    <tr>
    <td><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/raw/master/AestheticDialogs/flat-dark.png"/></td>
    <td colspan="2"><img src="https://github.com/gabriel-TheCode/AndroidLibrariesAssets/raw/master/AestheticDialogs/presentation.png"/></td>
     
  </tr>
  </tr>
</table>

<a name="implementation"></a>
## Implementation
Implementation of Aesthetic Dialogs is simple. You can check [/app](/app) directory for demo. Let's have look on basic steps of implementation.
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
	implementation 'com.github.gabriel-TheCode:AestheticDialogs:1.3.8'
}
```

<a name="createDialog"></a>
### Create Dialog

You can create multiple dialogs by specifying the style of your component, the type, and the animation of alert you want to display to the user.
You can override the ```.setOnClickListener()``` method to add a particular event, however some dialogs do not need it.



**Example 1**: Flat Dialog

``` kotlin
                  AestheticDialog.Builder(this, DialogStyle.FLAT, DialogType.SUCCESS)
                        .setTitle("Title")
                        .setMessage("Message")
                        .setCancelable(false)
                        .setDarkMode(true)
                        .setGravity(Gravity.CENTER)
                        .setAnimation(DialogAnimation.SHRINK)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                                //actions...
                            }
                        })
                        .show()
```

**Example 2**: Emotion Dialog


``` kotlin
  AestheticDialog.Builder(this, DialogStyle.EMOTION, DialogType.ERROR)
                        .setTitle("Title")
                        .setMessage("Message")
                        .show()
```


**Optional methods**
- setCancelable()
- setDarkMode()
- setDuration()
- setGravity()
- setAnimation()

**Constants**

<table style="width:100%">
  <tr>
    <th>DIALOG STYLE</th>
    <th>DIALOG TYPE</th>
    <th>DIALOG ANIMATION</th>
  </tr>
  <tr>
    <td>RAINBOW<br/>FLAT<br/>CONNECTIFY<br/>TOASTER<br/>DRAKE<br/>EMOJI<br/>EMOTION<br/>
    </td>
    <td>SUCCESS<br/>ERROR<br/>WARNING<br/>INFO</td>
     <td>DEFAULT<br/>SLIDE_UP, SLIDE_DOWN<br/>SLIDE_LEFT, SLIDE_RIGHT<br/> SWIPE_LEFT, SWIPE_RIGHT<br/>IN_OUT<br/>CARD<br/> SHRINK<br/>SPLIT<br/>DIAGONAL<br/>SPIN<br/>WINDMILL<br/>FADE<br/>ZOOM</td>
  </tr>
</table>

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

<a name="license"></a>
## License

[![Open Source Love svg1](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badges/)

```


    Copyright 2019 TEKOMBO Gabriel

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

```
