'use strict';
app.factory('mainFactory', function($http,$q) {
	return {
		getAuthor:function(app) {
			return $http({
				url : '/get-author',
				method : 'POST',
				data : app
			}).then(function(response) {
				return(response.data);
			}, function(response) {
				console.log(response);
				return($q.reject(response));
			});
		}
	};
});