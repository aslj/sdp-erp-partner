app.controller('homeCtrl', ['$scope','$log','$window','$sce',
	function($scope,$log,$window,$sce) {
	
	$scope.action = 0;
	$scope.review = function(action) {
		return($scope.action===action);
	}
	
	$scope.goToExamplePage = function() {
		$log.log("...loading example page");
	}
	
}]);
