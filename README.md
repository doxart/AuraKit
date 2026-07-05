# EasyPops Easy Android Pop Up - Progress, Snack bar, Date picker, Dialog & more

![Java](https://img.shields.io/badge/with-java-orange?style=for-the-badge&logo=openjdk&logoColor=orange&labelColor=white) ![Android](https://img.shields.io/badge/for-android-green?style=for-the-badge&logo=android&labelColor=white) ![M3](https://img.shields.io/badge/compatible-material3-blue?style=for-the-badge&logo=materialdesign&logoColor=blue&labelColor=white) [![](https://jitpack.io/v/doxart/EasyPops.svg)](https://jitpack.io/#doxart/EasyPops)

EasyPops is a lightweight and easy-to-use Android library for creating customizable and visually appealing popup messages in your application. Whether you need a simple toast, a fancy alert, or a fully customizable popup, EasyPops makes it effortless to integrate popups into your project.

<img src="https://github.com/doxart/AuraKit/blob/master/showcase.gif">

---

## 🎉 Features

- Simple and intuitive API.
- Fully customizable popups (colors, text, animations, etc.).
- Supports multiple popup styles like progress, snackbars, dateResult pickers and dialogs.
- Lightweight and optimized for performance.
- Compatible with Android API level 24 and above.
- Built-in support for material design principles.

---

## 🚀 Installation

### Step 1: Add the Dependency

Add the dependency to your app-level build.gradle:

```groovy
buildscript {
    repositories {
        google()
        mavenCentral()

        maven(url = "https://maven.google.com")
        maven(url = "https://jitpack.io")

    }
}
```

If still failing to resolve add this into your settings.gradle:

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven(url = "https://jitpack.io")
    }
}
```

Add the dependency to your module-level build.gradle:

[![](https://jitpack.io/v/doxart/EasyPops.svg)](https://jitpack.io/#doxart/EasyPops)

```groovy
dependencies {
    implementation ("com.github.doxart:EasyPops:latest")
}
```

## 📖 Usage

### Progress

```java
EasyPopup.createProgress(this)
     .buildAndShow();
```

<p align="center">
    <img width="300" align="center" src="https://github.com/doxart/EasyPops-Easy-Android-Pop-Up/blob/master/res/progress.gif">
</p>

#### Customizing

```java
EasyPopup.createProgress(this)
     .setProgressColor(ContextCompat.getColor(this, R.color.primary))
     .setAutoCancel(10000)
     .buildAndShow();
```

##

### Snack

```java
EasyPopup.createSnack(this)
     .setSnackStyle(SnackStyle.SNACK_ERROR(this))
     .setMessage("This is error snack.")
     .buildAndShow();
```

<p align="center">
    <img width="300" align="center" src="https://github.com/doxart/EasyPops-Easy-Android-Pop-Up/blob/master/res/snack.gif">
</p>

#### Customizing

```java
SnackStyle snackStyle = new SnackStyle();
snackStyle.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
snackStyle.setMessageColor(ContextCompat.getColor(this, R.color.white));
snackStyle.setButtonColor(ContextCompat.getColor(this, R.color.primary));
snackStyle.setPosition(SnackStyle.Position.BOTTOM);
```

or use direct styling

```java
EasyPopup.createSnack(this)
     .setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
     .buildAndShow();
```

##

### Date Picker

```java
DatePickerStyle datePickerStyle = new DatePickerStyle();
datePickerStyle.setBackgroundColor(ContextCompat.getColor(this, R.color.black));

EasyPopup.createDatePicker(this)
     .setTitle("Date picker")
     .setMessage("Select your dateResult")
     .setDatePickerStyle(datePickerStyle)
     .setOnDateSelectListener(new OnDateSelectListener() {
     @Override
     public void onDateSelected(int day, int month, int year) {
          String dateResult = day + "/" + month + "/" + year;
     }

     @Override
     public void onDismiss() {
     }
}).buildAndShow();
```

<p align="center">
    <img width="300" align="center" src="https://github.com/doxart/EasyPops-Easy-Android-Pop-Up/blob/master/res/datepicker.gif">
</p>

#### Customizing

```java
DatePickerStyle datePickerStyle = new DatePickerStyle();
datePickerStyle.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
datePickerStyle.setTitleColor(ContextCompat.getColor(this, R.color.white));
datePickerStyle.setMessageColor(ContextCompat.getColor(this, R.color.white));
datePickerStyle.setPositiveButtonColor(ContextCompat.getColor(this, R.color.primary));
datePickerStyle.setPositiveButtonTextColor(ContextCompat.getColor(this, R.color.white));
```

or use direct styling

```java
EasyPopup.createDatePicker(this)
     .setBackgroundColor(ContextCompat.getColor(this, R.color.white))
     .buildAndShow();
```

##

### Dialog

```java
EasyPopup.createDialog(this)
     .setTitle("Title")
     .setMessage("This is message")
     .setDialogStyle(DialogStyle.DIALOG_NORMAL())
     .buildAndShow();
```

<p align="center">
    <img width="300" align="center" src="https://github.com/doxart/EasyPops-Easy-Android-Pop-Up/blob/master/res/dialog.gif">
</p>

#### Customizing

```java
DialogStyle dialogStyle = new DialogStyle();
dialogStyle.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
dialogStyle.setTitleColor(ContextCompat.getColor(this, R.color.white));
dialogStyle.setMessageColor(ContextCompat.getColor(this, R.color.white));
dialogStyle.setPositiveButtonColor(ContextCompat.getColor(this, R.color.primary));
dialogStyle.setPositiveButtonTextColor(ContextCompat.getColor(this, R.color.white));
dialogStyle.setNegativeButtonColor(ContextCompat.getColor(this, R.color.primary));
dialogStyle.setNegativeButtonTextColor(ContextCompat.getColor(this, R.color.white));
```

or use direct styling

```java
EasyPopup.createDialog(this)
     .setTitleColor(ContextCompat.getColor(this, R.color.primary))
     .buildAndShow();
```

## 📦 Dependencies
Material Components
Ensures a modern look and feel that aligns with Android's material design principles.

```groovy
implementation "com.google.android.material:material:1.12.0"
```

## 📝 License
This library is licensed under the MIT License.
Feel free to use it in your personal and commercial projects.

## 🤝 Contributing
Contributions are welcome! If you have ideas, feature requests, or bug fixes, please create an issue or submit a pull request. Follow these steps to contribute:

Fork the repository.
Create a new branch for your feature (git checkout -b feature-name).
Commit your changes (git commit -m "Added feature X").
Push to the branch (git push origin feature-name).
Create a pull request.

## 🧑‍💻 Author
Developed with ❤️ by Doxart.
Feel free to reach out for support or collaboration opportunities.

## 🌟 Show Your Support
If you like this library, don't forget to give it a ⭐ on GitHub! 😊
