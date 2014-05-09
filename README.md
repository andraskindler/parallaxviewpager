ParallaxViewPager
=================

An easy-to-use ViewPager subclass with parallax background.

Setup requires little extra effort, using the ParallaxViewPager is just like using a standard ViewPager, with the same adapter. Of course, there's no silver bullet - the developer has to supply a background tailored to the current needs (eg. the number of items in the adapter and the size of the ViewPager). Check out <href="http://howrobotswork.files.wordpress.com/2014/05/parallaxviewpager.gif" target="_blank">this gif</a> to see what it's all about.

1. Include it in your project as a Gradle dependency:
```
    dependencies {
        compile 'com.andraskindler.parallaxviewpager:parallaxviewpager:0.1.3'
    }
```
2. Set the background with one of the following methods, or via xml:
  * *setBackgroundResource(int resid)*
  * *setBackground(Drawable background)* or *setBackgroundDrawable(Drawable background)*
  * *setBackground(Bitmap bitmap)*

3. (Optional) Specify how the view should scale the background with the *setScaleType(final int scaleType)* method. This only works with *FIT_HEIGHT*. Choose from the following parameters:
  * *FIT_HEIGHT* means the height of the image is resized to matched the height of the View, also stretching the width to keep the aspect ratio. The non-visible part of the bitmap is divided into equal parts, each of them sliding in at the proper position. This is the default value.
  * *FIT_WIDTH* means the width of the background image is divided into equal chunks, each taking up the whole width of the screen. This mode is not the usual parallax-effect, as the speed of the background scrolling equals the speed of the views.

4. (Optional) Set the amount of overlapping with the *setOverlapPercentage(final float percentage)* method. This is a number between 0 and 1, the smaller it is, the slower is the background scrolling. The default value is 50 percent.

An example, inside the *onCreate()* of an activity:

        //...
        final ParallaxViewPager parallaxViewPager = new ParallaxViewPager(this);
        parallaxViewPager.setAdapter(new MyPagerAdapter());
        parallaxViewPager.setBackgroundResource(R.drawable.nagy);
        setContentView(parallaxViewPager);
        //...

Other notices
=============

The lowest supported API level is 14 (Ice Cream Sandwich)

By **Andras Kindler** (andraskindler@gmail.com)
