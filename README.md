# Google's HTML (and XML) Compressor for Play Framework 2 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.fkoehler/play-html-compressor_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.fkoehler/play-html-compressor_2.13) [![Build Status](https://travis-ci.org/fkoehler/play-html-compressor.png)](https://travis-ci.org/fkoehler/play-html-compressor)

This project is a fork of http://github.com/mohiva/play-html-compressor.

## Installation

In your project/Build.scala:
```scala
libraryDependencies ++= Seq(
  "com.github.fkoehler" %% "play-html-compressor" % "2.9.0"
)
```

### History

* For Play Framework 2.9 use version 2.9.0
* For Play Framework 2.8 use version 2.8.0
* For Play Framework 2.7 use version 1.0.0
* For Play Framework 2.6 use version 0.7.1
* For Play Framework 2.5 use version 0.6.3
* For Play Framework 2.4 use version 0.5.0
* For Play Framework 2.3 use version 0.3.1
* For Play Framework 2.2 use version 0.2.1
* For Play Framework 2.1 use version 0.1-SNAPSHOT

## How to use

The filter comes with built-in `HtmlCompressor` and `XmlCompressor`
configurations, but it can also be used with user-defined configurations. The
following two examples shows how to define the filters with the default and the
user-defined configurations.

To provide the filters for your application you must define it as described in the Play
Documentation ([Scala](https://www.playframework.com/documentation/2.8.x/ScalaHttpFilters#Using-filters),
 [Java](https://www.playframework.com/documentation/2.8.x/JavaHttpFilters#Using-filters)).

### Provide filters

#### For Scala users

```scala
import javax.inject.Inject

import com.github.fkoehler.play.htmlcompressor.HTMLCompressorFilter
import com.github.fkoehler.play.xmlcompressor.XMLCompressorFilter
import play.api.http.HttpFilters
import play.api.mvc.EssentialFilter

class Filters @Inject() (
  htmlCompressorFilter: HTMLCompressorFilter,
  xmlCompressorFilter: XMLCompressorFilter)
  extends HttpFilters {

  override def filters: Seq[EssentialFilter] = Seq(
    htmlCompressorFilter,
    xmlCompressorFilter
  )
}
```

#### For Java users

```java
import com.github.fkoehler.play.htmlcompressor.HTMLCompressorFilter;
import com.github.fkoehler.play.xmlcompressor.XMLCompressorFilter;
import play.mvc.EssentialFilter;
import play.http.HttpFilters;

import javax.inject.Inject;

public class DefaultFilter implements HttpFilters {

    private HTMLCompressorFilter htmlCompressorFilter;
    private XMLCompressorFilter xmlCompressorFilter;

    @Inject
    public DefaultFilter(
        HTMLCompressorFilter htmlCompressorFilter,
        XMLCompressorFilter xmlCompressorFilter) {

        this.htmlCompressorFilter = htmlCompressorFilter;
        this.xmlCompressorFilter = xmlCompressorFilter;
    }

    @Override
    public EssentialFilter[] filters() {
        return new EssentialFilter[] {
            htmlCompressorFilter.asJava(),
            xmlCompressorFilter.asJava()
        };
    }
}

```

### Default filter

For the default filters we provide DI modules which will be automatically enabled if you
pull in the dependency. You must only provide your instance of `HttpFilters` as described
above.

### User-defined filter

For user defined filters there is a little bit mor to do. First you must create your instances of
the filter. As next you must provide your instance of `HttpFilters` as described above. At last
you must provide the bindings for you created filter and disable the default DI modules.

#### Implement filters

##### For Scala users

```scala
import javax.inject.Inject

import akka.stream.Materializer
import com.googlecode.htmlcompressor.compressor.HtmlCompressor
import com.github.fkoehler.play.htmlcompressor.HTMLCompressorFilter
import play.api.{Configuration, Environment, Mode}

class CustomHTMLCompressorFilter @Inject() (
  val configuration: Configuration, environment: Environment, val mat: Materializer)
  extends HTMLCompressorFilter {

  override val compressor: HtmlCompressor = {
    val c = new HtmlCompressor()
    if (environment.mode == Mode.Dev) {
      c.setPreserveLineBreaks(true)
    }

    c.setRemoveComments(true)
    c.setRemoveIntertagSpaces(false)
    c.setRemoveHttpProtocol(true)
    c.setRemoveHttpsProtocol(true)
    c
  }
}

```

##### For Java users

```java
import akka.stream.Materializer;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import com.github.fkoehler.play.htmlcompressor.HTMLCompressorFilter;
import play.Environment;
import play.Mode;
import play.api.Configuration;

import javax.inject.Inject;

public class CustomHTMLCompressorFilter extends HTMLCompressorFilter {

    private Configuration configuration;
    private Environment environment;
    private Materializer mat;

    @Inject
    public CustomHTMLCompressorFilter(
        Configuration configuration, Environment environment, Materializer mat) {

        this.configuration = configuration;
        this.environment = environment;
        this.mat = mat;
    }

    @Override
    public Configuration configuration() {
        return configuration;
    }

    @Override
    public HtmlCompressor compressor() {
        HtmlCompressor compressor = new HtmlCompressor();
        if (environment.mode() == Mode.DEV) {
            compressor.setPreserveLineBreaks(true);
        }

        compressor.setRemoveComments(true);
        compressor.setRemoveIntertagSpaces(true);
        compressor.setRemoveHttpProtocol(true);
        compressor.setRemoveHttpsProtocol(true);

        return compressor;
    }

    @Override
    public Materializer mat() {
        return mat;
    }

}

```

#### Provide bindings

To provide your bindings for your user defined filter you must either create a new module
or you can add the binding to your default DI module. This process is detailed documented
for [Scala](https://www.playframework.com/documentation/2.8.x/ScalaDependencyInjection) and
[Java](https://www.playframework.com/documentation/2.8.x/JavaDependencyInjection) users. So
please refer to this documentation.

##### Disable default modules

To disable the default modules you must append the modules to the `play.modules.disabled` property in `application.conf`:

```scala
play.modules.disabled += "com.github.fkoehler.play.htmlcompressor.HTMLCompressorFilterModule"
play.modules.disabled += "com.github.fkoehler.play.xmlcompressor.XMLCompressorFilterModule"
```

### Customize filter behaviour

You have the possibility to customize filter behaviour without using class inheritance. For
that, you could adding the following keys on your `application.conf` file :

```scala
play.filters {

  # Play Html Compressor
  # ~~~~~
  # https://github.com/fkoehler/play-html-compressor
  compressor {
    html {
      preserveLineBreaks = false
      removeComments = true
      removeIntertagSpaces = false
      removeHttpProtocol = true
      removeHttpsProtocol = true
    }

    xml {
      removeComments = true
      removeIntertagSpaces = true
    }
  }
}
```

### Release a new version

Follow the steps listed here: https://github.com/xerial/sbt-sonatype#publishing-your-artifact but basically it is

```
; + publishSigned; sonatypeBundleRelease
```

First the proper gnup keys need to be set locally