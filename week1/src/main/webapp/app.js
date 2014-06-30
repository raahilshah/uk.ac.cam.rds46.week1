var app = angular.module('app', []);

app.controller(
		'ResultsController',
		function($scope, $http) {

			$scope.action = function(a, b) {
				$scope.first = true;
				$scope.show = false;
				$scope.showRel = false;
				$http(
						{
							method : 'get',
							url : 'service/json/musicsearch?artist='
								+ a + '&song=' + b
						}).success(function(data) {
							$scope.results = data;
							$scope.show = true;
						}).error(function(data) {
							$scope.results = 'Error.';
						});
				$http(
						{
							method : 'get',
							url : 'http://ws.audioscrobbler.com/2.0/?method=track.getsimilar&artist='
								+ a
								+ '&track='
								+ b
								+ '&api_key=d03e58b2c37b049287c671bc7f451632&format=json'
						}).success(function(data) {
							$scope.related = data;
							$scope.showRel = true;
						}).error(function(data) {
							$scope.related = 'Error.';
						});
			};
		});

app.controller('ScrollController', function($scope, $location,
		$anchorScroll) {
	$scope.gotoResults = function() {
		// set the location.hash to the id of
		// the element you wish to scroll to.
		$location.hash('location');

		// call $anchorScroll()
		$anchorScroll();
	};
});
