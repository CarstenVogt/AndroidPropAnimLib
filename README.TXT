These libraries provide utitilies for programming with the Android property animation technique. A detailed explanation can be found here:

- YouTube Video 1 Overview: https://youtu.be/RYjBFQDCfPQ
- YouTube Video 2 Code:     https://www.youtube.com/watch?v=HTr16eRqq60
- YouTube Video 3 Demo:     https://www.youtube.com/watch?v=oHWNgBJRwCg

- JavaDoc: http://www.nt.th-koeln.de/vogt/vma/javadoc/propanimlib/de/thkoeln/cvogt/android/propanim_utilities/package-summary.html
           http://www.nt.th-koeln.de/vogt/vma/javadoc/propanimlib/de/thkoeln/cvogt/android/propanim_utilities/AnimatedGuiObjectCV.html
           http://www.nt.th-koeln.de/vogt/vma/javadoc/propanimlib/de/thkoeln/cvogt/android/propanim_utilities/AnimationViewCV.html

The libaries can be included in an Android Studio project by these steps:

1.) Include jitpack.io in build.gradle (Project):

allprojects { repositories { ... maven { url 'https://jitpack.io' } } }

2.) Specify a dependency on these libraries in build.gradle (Module):

dependencies { implementation 'com.github.CarstenVogt:AndroidPropAnimLib:..release number here..' ... }

