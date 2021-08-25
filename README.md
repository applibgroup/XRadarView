# XRadarView [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=applibgroup_XRadarView&metric=alert_status)](https://sonarcloud.io/dashboard?id=applibgroup_XRadarView) [![Build](https://github.com/applibgroup/XRadarView/actions/workflows/main.yml/badge.svg)](https://github.com/applibgroup/XRadarView/actions/workflows/main.yml)
XRadarView is a "A highly customizable radar view for Harmonyos". XRadarView is a highly customizable radar view control. 

## Attribute meaning
XRadarView supports a high degree of customization, and its adjustable attributes and meanings are shown in the table below.

|Attributes  | meaning |
|:-------------:|:-------------:|
|count|Polygonal edge count (note: Value must be less than or equal to nodelist size.)|
|layerCount|Several layers of spider webs|
|drawableSize|Icon size (Recommended size should be between 8vp - 26vp)|
|drawablePadding|Icon and text spacing|
|descPadding|Title description and distance between nodes|
|titleSize|Title text size (Recommended size should be between 10fp - 20fp)|
|radarPercent|The ratio of the radar chart graphic to the entire space|
|startColor|When the gradient is turned on, the color at the center of the circle|
|endColor|When the gradient is turned on, the color at the outer circle|
|cobwebColor|The color of the regular polygonal network cable|
|singleColor|If it is not a multi-color area, it is a single color|
|titleColor|The color of the title text|
|pointColor|Dot color|
|borderColor|Border color|
|radiusColor|The color of the radius line|
|boundaryWidth|The width of the border line|
|pointRadius|Dot radius size|
|enabledBorder|Whether to draw a boundary line|
|enabledAnimation|Whether to turn on animation|
|enabledShowPoint|Whether to show dots|
|enabledPolygon|Whether to draw a grid|
|enabledShade|Whether to draw a gradient ring|
|enabledRadius|Whether to draw radius|
|enabledText|Whether to draw text|
|enabledCircularOutline|Whether the area outline is circular|
|enabledMultiColorRegion|Whether the region is draw with multi-color|
|enabledRegionShader|Whether to allow regional color gradients|
|nodeList|Node is a model class which include all the details of single node (i.e. title, value, percent, iconRef, colorValue). As per your requirement you can add element in node list, but the size of node list must be greater than or equal to count value. And You can set node list only from java file using setNodeList(List<Node> nodeList).|



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
        ohos:count="7" //Value must be less than or equal to nodelist size.
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
    
        // Set title color
        radarView.setTitleColor(Color.RED);
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
        
        // create node list
        ArrayList<Node> nodeList = new ArrayList<>();
        nodeList.add(new Node("Kill", "80", 0.8, ResourceTable.Media_icon, RgbPalette.parse("#A0ffcc00")));
        nodeList.add(new Node("Money", "80%", 0.8, ResourceTable.Media_icon, RgbPalette.parse("#A000ff00")));
        nodeList.add(new Node("Survival", "0.9", 0.9, ResourceTable.Media_icon, RgbPalette.parse("#A00000ff")));
        nodeList.add(new Node("Defense", "100%", 1, ResourceTable.Media_icon, RgbPalette.parse("#A0FF00FF")));
        
        xRadarView.setNodeList(nodeList);
        
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
