'use strict';

/* Controllers */

angular.module('sres.controllers', []).
        controller('UserCtrl',
                ['$scope', '$http',
                    function($scope, $http) {

                        $scope.users = [];
                        $scope.counter = 0;
                        
                        var url='',method='';
                        if (window.durl) {
                            url=window.durl;
                            //method='jsonp'
                        } else {
                            url='rest/users';
                            //method='get';
                        }
                        
                        $http.get(url).success(function(data) {
                            var users = data.users ? data.users : data;
                            for (var i = 0; i < users.length; i++) {
                                users[i].id = i+1;
                               }
                               
                               $scope.users=users;
                        });


                    }]).
        controller('ReportCtrl',
                ['$scope', '$http','$routeParams',
                    function($scope, $http,$routeParams) {
                        console.log($routeParams);
                    }]);
        
        