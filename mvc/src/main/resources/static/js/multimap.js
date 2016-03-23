
/*<![CDATA[*/

function renderGoogleMap() {
	var start_point = new google.maps.LatLng(0, 0);

	// Creating a new map
	var map = new google.maps.Map(document.getElementById("map_canvas"), {
		center : start_point,
		zoom : 5,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	});

	// Creating a global infoWindow object that will be reused by all markers
	var infoWindow = new google.maps.InfoWindow();

	function setMarkerPoints(map, marker) {
		var bounds = new google.maps.LatLngBounds();

		$.ajax({
			type : "GET",
			url : '/products/json',
			dataType : "json",
			success : function(data) {

				if (data.length !== 0) {

					$.each(data, function(marker, data) {

						var latLng = new google.maps.LatLng(data.point.x,
								data.point.y);
						bounds.extend(latLng);

						// Creating a marker and putting it on the map
						var marker = new google.maps.Marker({
							position : latLng,
							map : map,
							title : data.title
						});

						var windowContent = '<h3>' + data.id + '</h3>' + '<p>'
								+ data.location + '</p>';

						// Attaching a click event to the current marker
						infobox = new InfoBox({
							content : infoWindow.setContent(windowContent),
							alignBottom : true,
							pixelOffset : new google.maps.Size(-160, -45)
						});

						google.maps.event.addListener(marker, 'click',
								function() {

									// Open this map's infobox
									infobox.open(map, marker);
									infobox.setContent(windowContent);
									map.panTo(marker.getPosition());
									infobox.show();
								});
						google.maps.event.addListener(map, 'click', function() {
							infobox.setMap(null);
						});
					});

				}
			},
			error : function(data) {
				console.log('Please refresh the page and try again');
			}
		});
		// END MARKER DATA

		// end loop through json

		map.setCenter(start_point);
		map.fitBounds(bounds);
	}
	setMarkerPoints(map);
}

google.maps.event.addDomListener(window, 'ready', renderGoogleMap);

/* ]]> */


