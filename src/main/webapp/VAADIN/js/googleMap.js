if (typeof Tila != "undefined" && typeof Tila.map != "undefined") {
	delete Tila.map;
}

var tilaGoogleCacheUrl = "$tilaGoogleCacheUrl";

var latlng = new google.maps.LatLng(36.300, -6.500);

var googleOptions = {
		zoom: 8,
		center: latlng,
		mapTypeControlOptions: { mapTypeIds: ['Road', 'Satelite'] }
};



var roadTypeOptions = {
		getTileUrl: function(coord, zoom) {
			var url = tilaGoogleCacheUrl + "/vt/x=" + coord.x + "&y=" + coord.y + "&z=" + zoom; 
			return url;
		},
		tileSize: new google.maps.Size(256, 256),
		isPng: true,
		maxZoom: 22,
		minZoom: 0,
		name: 'Road'
};

var sateliteTypeOptions = {
		getTileUrl: function(coord, zoom) {
			var url = tilaGoogleCacheUrl + "/kh/v=106&x=" + coord.x + "&y=" + coord.y + "&z=" + zoom;    	        
			return url;
		},
		tileSize: new google.maps.Size(256, 256),
		isPng: true,
		maxZoom: 22,
		minZoom: 0,
		name: 'Satelite'
};


var roadMapType = new google.maps.ImageMapType(roadTypeOptions);
var sateliteMapType = new google.maps.ImageMapType(sateliteTypeOptions);


var googleMap = new google.maps.Map(document.getElementById("map"), googleOptions);

googleMap.mapTypes.set('Road', roadMapType);
googleMap.mapTypes.set('Satelite', sateliteMapType);
googleMap.setMapTypeId('Road');
