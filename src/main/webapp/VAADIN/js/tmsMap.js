if (typeof googleMap != "undefined") {
 	document.getElementById("map").innerHTML = "";
 	delete googleMap;
 }
 

if (typeof Tila == "undefined" || !Tila) {
    var Tila = {};
}

if (typeof Tila.map == "undefined" || !Tila.map) {
	Tila.map = new OpenLayers.Map('map');
}
else {
	Tila.map.removeLayer(Tila.layer);
}

Tila.layer = new OpenLayers.Layer.TMS (
		"$layerName", 			// name for display in LayerSwitcher
		"$cacheUrl",            // service endpoint
		{layername: "$layerName", type: "png", projection: "$srs"} 
);

Tila.map.addLayer(Tila.layer);
Tila.map.setBaseLayer(Tila.layer);
Tila.map.zoomToMaxExtent();   

