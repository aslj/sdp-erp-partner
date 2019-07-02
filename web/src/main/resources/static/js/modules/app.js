var app = angular.module('mainApp',['ngRoute','ngResource','ui.bootstrap','angularFileUpload','timer','ngAnimate']); 

app.constant('STEP', {
	FIRST: 1,
	SECOND: 2,
	THIRD: 3
});

//declare config
app.config(['$logProvider','$provide','$routeProvider',function($logProvider,$provide,$routeProvider) {
	// setup of route view
	$routeProvider.when('/home', {
		templateUrl : '/views/home.html',
		controller : 'homeCtrl'
	}).when('/example', {
		templateUrl : '/views/example.html',
		controller : 'exampleCtrl'	
	}).otherwise({
		redirectTo : '/home'
	});
}]);

//application startup hook
app.run(['$rootScope','$location','$http','$route','$log',function ($rootScope,$location,$http,$route,$log) {
	$log.log("Main App is running");
}]);

app.directive('errSrc', function() {
	return {
		link: function(scope, element, attrs) {
			element.bind('error', function() {
				if (attrs.src != attrs.errSrc) {
					attrs.$set('src', attrs.errSrc);
				}
			});
		}
	}
});
