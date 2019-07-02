const AUTHOR = 'Osain Dabián';

app.controller('mainCtrl', ['$q','$scope','$log','$window','$location','mainFactory',
	function($q,$scope,$log,$window,$location,mainFactory) {
	
	$scope.author = AUTHOR;
	$log.log("Front End Author: "+$scope.author);
	$scope.year = new Date().getFullYear();

	// WAIT UNTIL DOCUMENTO IS READY
	angular.element(document).ready(function () {
	
	var app = {'frontEnd':'Main App'};
	mainFactory.getAuthor(app).then(function(data) {
		$log.log("Back End Author: "+data.author);
	}, function(errResponse) {
		$log.log(errResponse);
		$log.error('*Error* while getAuthor');
	});

	$scope.logout = function() {
		$window.location.assign('/logout');	//LOGOUT FROM BACK AND FRONT
	}
	
	$scope.paymentTypes = [ 
		{'id':'0', 'name':'undefined'}, 
		{'id':'1', 'name':'cash'}, 
		{'id':'2', 'name':'check'}, 
		{'id':'3', 'name':'transfer'}];

	});// END OF { WAIT UNTIL DOCUMENTO IS READY}

}]);
