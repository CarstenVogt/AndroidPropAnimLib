These libraries provide utitilies for programming with the Android property animation technique.

The libaries can be included in an Android Studio project by these steps:

1.) Include jitpack.io in build.gradle (Project):

allprojects { repositories { ... maven { url 'https://jitpack.io' } } }

2.) Specify a dependency on these libraries in build.gradle (Module):

dependencies { implementation 'com.github.CarstenVogt:AndroidPropAnimLib:..release number here..' ... }

