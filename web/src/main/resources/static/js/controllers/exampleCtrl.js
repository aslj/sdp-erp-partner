app.controller('exampleCtrl', ['$scope','$log','$window','$sce',
	function($scope,$log,$window,$sce) {
	
	$scope.action = 0;
	$scope.review = function(action) {
		return($scope.action===action);
	}
	
	$scope.goToHomePage = function() {
		$log.log("...loading home page");
	}
	
}]);
