Development stopped in 2014
=================
Not developed since 2014. Unfinished and not stable - not recommended to use.

ParallaxViewPager
=================

An easy-to-use ViewPager subclass with parallax background.

Setup requires little extra effort, using the ParallaxViewPager is just like using a standard ViewPager, with the same adapter. Of course, there's no silver bullet - the developer has to supply a background tailored to the current needs (eg. the number of items in the adapter and the size of the ViewPager).

1. Include it in your project as a Gradle dependency:
```
    dependencies {
        compile 'com.andraskindler.parallaxviewpager:parallaxviewpager:0.4.0'
    }
```
2. Create a ParallaxViewPager programmatically or in a layout xml.

3. Set the background via xml or one of the following methods:
  * *setBackgroundResource(int resid)*
  * *setBackground(Drawable background)* or *setBackgroundDrawable(Drawable background)*
  * *setBackground(Bitmap bitmap)*

4. (Optional) Specify how the view should scale the background with the *setScaleType(final int scaleType)* method. Choose from the following parameters:
  * *FIT_HEIGHT* means the height of the image is resized to matched the height of the View, also stretching the width to keep the aspect ratio. The non-visible part of the bitmap is divided into equal parts, each of them sliding in at the proper position. This is the default value.
  * *FIT_WIDTH* means the width of the background image is divided into equal chunks, each taking up the whole width of the screen. This mode is not the usual parallax-effect, as the speed of the background scrolling equals the speed of the views.

5. (Optional) Set the amount of overlapping with the *setOverlapPercentage(final float percentage)* method. This is a number between 0 and 1, the smaller it is, the slower is the background scrolling. The default value is 50 percent. This only works with *FIT_HEIGHT*.

An example, inside the *onCreate()* of an activity:

        //...
        final ParallaxViewPager parallaxViewPager = new ParallaxViewPager(this);
        parallaxViewPager.setAdapter(new MyPagerAdapter());
        parallaxViewPager.setBackgroundResource(R.drawable.nagy);
        setContentView(parallaxViewPager);
        //...

6. If you are using a the [ViewPagerIndictor](https://github.com/JakeWharton/ViewPagerIndicator) library to enable progress of a the ViewPager, you will need to also set the ```OnPageChangeListener``` to include the the ```ParallaxViewPager```'s listener. In your ```Activity.onCreate``` or ```Fragment.onCreateView```:

```
circlePageIndicator.setOnPageChangeListener(viewPager
            .getPageChangeListener());
```


Other notices
=============

The lowest supported API level is 14 (Ice Cream Sandwich)

By **Andras Kindler** (andraskindler@gmail.com)

License
=======

    Copyright 2014 Andras Kindler

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
