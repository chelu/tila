 if (typeof googleMap != "undefined") {
 	document.getElementById("map").innerHTML = "";
 	delete googleMap;
 }
 
if (typeof Tila == "undefined" || !Tila) {
    alert("tila defined");
    var Tila = {};
}

if (typeof Tila.map == "undefined" || !Tila.map) {
	Tila.map = new OpenLayers.Map('map');
}
else {
	Tila.map.removeLayer(Tila.layer);
}


Tila.layer = new OpenLayers.Layer.OSM (null, "$cacheUrl/${z}/${x}/${y}.png");

Tila.map.addLayer(Tila.layer);
Tila.map.setBaseLayer(Tila.layer);
Tila.map.zoomToMaxExtent();
