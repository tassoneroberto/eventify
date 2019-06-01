function getOwnedEvents(account_id, visitor) {
	var data = {
		"api_key": "hYzFgaX5libG1QeAOxjbfwKRJMMLj2TH",
		"operation": "getOwnedEvents",
		"account_id": account_id
	}
	$.ajax({
		url: 'https://apisocialevent.altervista.org/API/',
		type: 'POST',
		data: JSON.stringify(data),
		contentType: 'application/json',
		dataType: 'json',
		async: true,
		success: function (response) {

			var categoryCount = new Map();
			categoryCount.set("Food & Drink", 0);
			categoryCount.set("Festival", 0);
			categoryCount.set("Disco", 0);
			categoryCount.set("Live Music", 0);
			categoryCount.set("Theatre", 0);
			categoryCount.set("Museum", 0);
			categoryCount.set("Outdoor", 0);
			categoryCount.set("Cinema", 0);

			eventList = response.events;

			var eventsCalendar = [];

			if (eventList) {
				for (var i = 0; i < eventList.length; i++) {

					if (visitor == undefined)
						buildOrganizerCard(eventList[i], i);
					else
						buildUserCard(eventList[i], i);
					e = eventList[i];
					var color;
					if (e.category == "Festival") { color = '#0A6DBB'; }
					else if (e.category == "Outdoor") { color = '#2E7D32'; }
					else if (e.category == "Food & Drink") { color = '#B71C1C'; }
					else if (e.category == "Theatre") { color = '#827717'; }
					else if (e.category == "Museum") { color = '#795548'; }
					else if (e.category == "Live Music") { color = '#3F51B5'; }
					else if (e.category == "Disco") { color = '#AD1457'; }
					else { color = '#000000'; }

					eventsCalendar.push({
						'title': eventList[i].title,
						'allDay': true,
						'start': eventList[i].opening,
						'end': eventList[i].ending,
						'color': color
					});

					var eventCategory = eventList[i].category;
					if (!categoryCount.has(eventCategory)) {
						categoryCount.set(eventCategory, 1);
					}
					else {
						var oldValue = categoryCount.get(eventCategory);
						categoryCount.set(eventCategory, parseInt(oldValue) + 1);
					}
				}
			}

			var events = {
				events: eventsCalendar
			};
			$('#calendar').fullCalendar('addEventSource', events);


			var chart = new CanvasJS.Chart("chartContainer",
				{
					backgroundColor: "transparent",

					title: {

						text: "Percentage chart of Categories",
						fontWeight: "normal",
						fontSize: 36,
						fontColor: "black",
					},

					legend: {
						verticalAlign: "bottom",
						horizontalAlign: "center"
					},
					data: [
						{
							//startAngle: 45,
							indexLabelFontSize: 20,
							indexLabelFontFamily: "Roboto",
							indexLabelFontColor: "black",
							indexLabelLineColor: "black",
							indexLabelPlacement: "outside",
							type: "doughnut",
							showInLegend: true,
							dataPoints: [
								{ y: parseInt(categoryCount.get("Food & Drink")), legendText: "Food & Drink", indexLabel: "Food & Drink" },
								{ y: parseInt(categoryCount.get("Festival")), legendText: "Festival", indexLabel: "Festival" },
								{ y: parseInt(categoryCount.get("Disco")), legendText: "Disco", indexLabel: "Disco" },
								{ y: parseInt(categoryCount.get("Live Music")), legendText: "Live Music", indexLabel: "Live Music" },
								{ y: parseInt(categoryCount.get("Theatre")), legendText: "Theatre", indexLabel: "Theatre" },
								{ y: parseInt(categoryCount.get("Museum")), legendText: "Museum", indexLabel: "Museum" },
								{ y: parseInt(categoryCount.get("Outdoor")), legendText: "Outdoor", indexLabel: "Outdoor" },
								{ y: parseInt(categoryCount.get("Cinema")), legendText: "Cinema", indexLabel: "Cinema" }

							]
						}
					]
				});


			chart.render();


			if (eventList) {
				var num = eventList.length;
				if (num > 10) num = 10;
				var sortedArr = eventList.sort(function (l, r) { return r.followers - l.followers; }).slice(0, num);
			}


			var topten = [];
			for (var i = 0; i < num; i++) {

				topten.push({
					'label': sortedArr[i].title,
					'y': parseInt(sortedArr[i].followers),

				});
			}



			var topEvents = new CanvasJS.Chart("topEventsChartContainer", {
				backgroundColor: "transparent",
				title: {
					text: "Most followed events",
					fontWeight: "normal",
					fontSize: 36, fontColor: "black",



				},
				axisY: {
					labelFontColor: "black",
					lineColor: "white",

					tickColor: "white",
					gridColor: "white",
					gridThickness: 1,
					interval: 1,

				},
				axisX: {
					lineColor: "white",

					labelFontColor: "black",
					tickColor: "white",
					gridColor: "white",
					gridThickness: 1,

				},
				data: [//a
					{ //dataSeries object
						indexLabelFontSize: 20,
						indexLabelFontFamily: "Roboto",
						indexLabelFontColor: "black",
						indexLabelLineColor: "black",
						/*** Change type "column" to "bar", "area", "line" or "pie"***/
						type: "bar",
						dataPoints: topten



					}
				]
			});

			topEvents.render();




		}
	});
}

function removeEvent(event_id, card_id) {
	var data = {
		"api_key": "hYzFgaX5libG1QeAOxjbfwKRJMMLj2TH",
		"operation": "removeEvent",
		"event_id": event_id
	}
	$.ajax({
		url: 'https://apisocialevent.altervista.org/API/',
		type: 'POST',
		data: JSON.stringify(data),
		contentType: 'application/json',
		dataType: 'json',
		async: true,
		success: function (response) {
			if (response.result == "success")
				document.getElementById(card_id).style.display = 'none';
		}
	});
}