# XRadarView [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=applibgroup_XRadarView&metric=alert_status)](https://sonarcloud.io/dashboard?id=applibgroup_XRadarView) [![Build](https://github.com/applibgroup/XRadarView/actions/workflows/main.yml/badge.svg)](https://github.com/applibgroup/XRadarView/actions/workflows/main.yml)
XRadarView is a "A highly customizable radar view for Harmonyos". XRadarView is a highly customizable radar view control. 

## Attribute meaning
XRadarView supports a high degree of customization, and its adjustable attributes and meanings are shown in the table below.

|Attributes  | meaning |
|:-------------:|:-------------:|
|count|Polygonal edge count|
|layerCount|Several layers of spider webs|
|drawableSize|Icon size|
|drawablePadding|Icon and text spacing|
|descPadding|Title description and distance between nodes|
|titleSize|Title text size|
|radarPercent|The ratio of the radar chart graphic to the entire space|
|startColor|When the gradient is turned on, the color at the center of the circle|
|endColor|When the gradient is turned on, the color at the outer circle|
|cobwebColor|The color of the regular polygonal network cable|
|singleColor|If it is not a multi-color area, it is a single color|
|titleColor|The color of the title text|
|pointColor|Dot color|
|pointRadius|Dot radius size|
|borderColor|Border color|
|boundaryWidth|The width of the border line|
|radiusColor|The color of the radius line|
|enabledBorder|Whether to draw a boundary line|
|enabledAnimation|Whether to turn on animation|
|enabledShowPoint|Whether to show dots|
|enabledPolygon|Whether to draw a grid|
|enabledShade|Whether to draw a gradient ring|
|enabledRadius|Whether to draw radius|
|enabledText|Whether to draw text|
|drawables|An array of icons|
|titles|Title array|
|percents|The value array of each item (converted to a value between 0-1)|
|values|Text array of values|
|colors|In the case of multi-color areas, the color array of each area (the length of the array can be less than count)|
|enabledRegionShader|Whether to allow regional color gradients|
|enabledCircularOutline|Whether the area outline is circular|

## Screenshots
![](https://github.com/applibgroup/XRadarView/blob/master/screenshot/Screenshot%20(1).png)
![](https://github.com/applibgroup/XRadarView/blob/master/screenshot/Screenshot%20(2).png)
![](https://github.com/applibgroup/XRadarView/blob/master/screenshot/Screenshot%20(3).png)
![](https://github.com/applibgroup/XRadarView/blob/master/screenshot/Screenshot%20(4).png)

## How to use

#### Installation
1. For using XRadarView module in sample app, include the source code and add the below dependencies in entry/build.gradle to generate hap/support.har.
```
	dependencies {
		implementation project(':xradar')
        	implementation fileTree(dir: 'libs', include: ['*.har'])
        	testCompile 'junit:junit:4.12'
	}
```
2. For using XRadarView in separate application using har file, add the har file in the entry/libs folder and add the dependencies in entry/build.gradle file.
```
	dependencies {
		implementation fileTree(dir: 'libs', include: ['*.har'])
		testCompile 'junit:junit:4.12'
	}

```
3. For using XRadarView from a remote repository in separate application, add the below dependencies in entry/build.gradle file.
```
	dependencies {
		implementation 'dev.applibgroup:xradar:1.0.0'
		testCompile 'junit:junit:4.12'
	}
```

## Add XRadarView in layout file: 

    <com.orzangleli.radar.XRadarView
        ohos:id="$+id:radarView"
        ohos:height="match_parent"
        ohos:width="match_parent"
        ohos:count="7"
        ohos:layerCount="4"
        ohos:enabledBorder="true"
        ohos:borderColor="#ff0000"
        ohos:boundaryWidth="4"
        ohos:radiusColor="#88D3D8"
        ohos:enabledShowPoint="false"
        ohos:startColor="#278892"
        ohos:endColor="#D4F0F2"
        ohos:titleColor="#000"
        ohos:descPadding="16"
        />
        
## Configure runtime in java file:

    XRadarView radarView = (XRadarView) this.findComponentById(ResourceTable.Id_radarView);
    
        // The scale of values in the radar chart
        radarView.setPercents(percents);
        // Sets an array of colors for each area
        // If colors are set, each area will display a different color, otherwise all areas will display the same color Action 1
        radarView.setColors(null);
        // Set each title
        radarView.setTitles(titles);
        // Set title color
        radarView.setTitleColor(Color.RED);
        // Set the text of the values displayed by each item
        radarView.setValues(values);
        // Set the icon for each item
        radarView.setDrawables(drawables);
        // Set the drawable padding for each item
        radarView.setDrawablePadding(20);
        // The setting allows connections between points
        radarView.setEnabledBorder(true);
        // Show dots
        radarView.setEnabledShowPoint(true);
        // Set dots radius
        radarView.setPointRadius(8);
        // Set dots color
        radarView.setPointColor(Color.RED);
        // Draw positive n deformation
        radarView.setEnabledPolygon(true);
        // Draws a gradient ring
        radarView.setEnabledShade(true);
        // Draw the radius
        radarView.setEnabledRadius(true);
        // Draw titles, values, icons, etc
        radarView.setEnabledText(true);
        // Turn on the animation
        radarView.setEnabledAnimation(true);
        // Set the number of layers
        radarView.setLayerCount(5);
        // A single color without gradient colors
        radarView.setSingleColor(new Color(RgbPalette.parse("#800000ff")));
        
## Future Work
1. Adding RichClientText support.
2. Customize data value style.

## License
MIT License

## Everything
Support rotation angle
Support custom title and radar chart distance  

## Original Repository [Android] 
https://github.com/hust201010701/XRadarView

Enjoy it!:smile:      
