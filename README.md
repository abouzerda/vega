<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![Kotlin][kotlin-shield]][kotlin-url]
[![OpenGL][opengl-shield]][opengl-url]

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/othneildrew/Best-README-Template">
    <img src="assets/images/logo.png" alt="Logo" width="150" height="150">
  </a>

<h3 align="center">VEGA</h3>

  <p align="center">
    An OpenGL game engine written in Kotlin
  </p>
</div>

## Getting Started

### Installing

In order to use Vega you will need to clone the repo and publish to Maven Local.
Then you can include Vega as a dependency from maven local into your project.

```shell
[user@computer ~]$ git clone https://github.com/heaterscar/vega.git
[user@computer ~]$ cd vega
[user@computer vega]$ ./gradlew publishToMavenLocal
```

### Dependencies

Be sure to define the local maven repository in your `build.gradle.kts` in order for this to work.

```kotlin
repositories { 
    mavenCentral()
    /* Vega */
    mavenLocal()
    /* Dear ImGui */
    jcenter()
}

dependencies {
    implementation("com.example", "vega","1.0-SNAPSHOT")
}
```

### Executing program

In order to create an application you have to inherit from `LWJGLApplication`
and create a new scene. Then run your application from the main function.

```kotlin
fun main() { 
    Application.run()
}

object Application : LWJGLApplication() {
    var scene: Scene = MainScene()

    init {
        showScene(scene)
    }
}
```
## Help

This game engine is still in pre-alpha and lacks a lot of features.
It is still in development and probably has a lot of bugs.
It's primarily used for our own game development.  

So if you encounter any problems or you want to request a feature be sure to [contact us](https://github.com/heaterscar/vega/issues).


## Authors

Contributors names and contact info

Amin Bouzerda  
[@heaterscar](https://github.com/heaterscar)

## Version History

* 1.0-SNAPSHOT
    * Initial Release

## License

This project is licensed under the MIT License - see the LICENSE file for details

## Acknowledgments

Inspiration, code snippets, etc.
* [GamesWithGabe](https://www.youtube.com/c/GamesWithGabe)
* [ocornut](https://github.com/ocornut/imgui)
* [othneildrew](https://github.com/othneildrew/Best-README-Template)
* [DomPizzie](https://gist.github.com/DomPizzie/7a5ff55ffa9081f2de27c315f5018afc)

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/heaterscar/vega?style=for-the-badge&color=blue
[contributors-url]: https://github.com/heaterscar/vega/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/heaterscar/vega?style=for-the-badge
[forks-url]: https://github.com/heaterscar/vega/network/members
[stars-shield]: https://img.shields.io/github/stars/heaterscar/vega?style=for-the-badge
[stars-url]: https://github.com/heaterscar/vega/stargazers
[issues-shield]: https://img.shields.io/github/issues/heaterscar/vega?style=for-the-badge
[issues-url]: https://github.com/heaterscar/vega/issues
[license-shield]: https://img.shields.io/github/license/heaterscar/vega?style=for-the-badge
[license-url]: https://github.com/heaterscar/vega/blob/master/LICENSE
[kotlin-shield]: https://img.shields.io/badge/kotlin-%230095D5.svg?style=for-the-badge&logo=kotlin&logoColor=white
[kotlin-url]: https://kotlinlang.org/
[opengl-shield]: https://img.shields.io/badge/OpenGL-%23FFFFFF.svg?style=for-the-badge&logo=opengl
[opengl-url]: https://www.opengl.org/