//Create a google map on MapViewer custom component

var tilaGoogleCacheUrl = "$tilaGoogleCacheUrl";

var latlng = new google.maps.LatLng(36.300, -6.500);
var myOptions = {
		zoom: 8,
		center: myLatlng,
		mapTypeControlOptions: { mapTypeIds: ['Road', 'Satelite'] }
};

var roadTypeOptions = {
		getTileUrl: function(coord, zoom) {
			var url = tilaGoogleCacheUrl + "/vt/x=" + coord.x + "&y=" + coord.y + "&z=" + zoom;    	        
			return url;
		},
		tileSize: new google.maps.Size(256, 256),
		isPng: true,
		maxZoom: 9,
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
		maxZoom: 12,
		minZoom: 0,
		name: 'Satelite'
};


var roadMapType = new google.maps.ImageMapType(roadTypeOptions);
var sateliteMapType = new google.maps.ImageMapType(sateliteTypeOptions);


var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
map.mapTypes.set('Road', roadMapType);
map.mapTypes.set('Satelite', sateliteMapType);
map.setMapTypeId('Road');
