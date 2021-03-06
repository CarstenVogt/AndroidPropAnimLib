This is a utility library to facilitate programming with the Android property animation technique (directory: propanim_utilities) together with a demo app that demonstrates the features of this library (directory: app). The complete Android Studio project can be downloaded through the green button on top (code > download ZIP).

A detailed explanation of the library can be found here:

YouTube videos:
- Part 1 Overview: https://youtu.be/RYjBFQDCfPQ
- Part 2 Code: https://youtu.be/HTr16eRqq60
- Part 3 Demo: https://youtu.be/oHWNgBJRwCg

JavaDoc: 
- Start page: http://www.nt.th-koeln.de/vogt/vma/javadoc/propanimlib/
- Class AnimatedGuiObjectCV: http://www.nt.th-koeln.de/vogt/vma/javadoc/propanimlib/de/thkoeln/cvogt/android/propanim_utilities/AnimatedGuiObjectCV.html
- Class AnimationViewCV: http://www.nt.th-koeln.de/vogt/vma/javadoc/propanimlib/de/thkoeln/cvogt/android/propanim_utilities/AnimationViewCV.html

The library can be included in an Android Studion project by these steps:

1.a) (Before Android Studio Arctic Fox / Gradle 7.0)
Include jitpack.io in build.gradle (Project):
allprojects { repositories { ... maven { url 'https://jitpack.io' } } }

1.b) (For Android Studio Arctic Fox / Gradle 7.0)
Include jitpack.io in settings.gradle:
dependencyResolutionManagement { ... repositories { ... maven { url 'https://jitpack.io' } } }

2.) Specify a dependency on these libraries in build.gradle (Module):

dependencies {
  implementation 'com.github.CarstenVogt:AndroidPropAnimLib:...releasenumber...'
  ...
}

The work is provided by Prof. Dr. Carsten Vogt, Technische Hochschule K&ouml;ln, Fakult&auml;t f&uuml;r Informations-, Medien- und Elektrotechnik, Germany
under GPLv3, the GNU General Public License 3,
http://www.gnu.org/licenses/gpl-3.0.html.


