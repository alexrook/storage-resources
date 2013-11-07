'use strict';

/* Controllers */

angular.module('sres.controllers', []).
        controller('UserCtrl',
                ['$scope', '$http',
                    function($scope, $http) {

                        $scope.users = [];
                        $scope.counter = 0;
                        $http.get('rest/users').success(function(data) {
                            var users = data.users ? data.users : data;
                            for (var i = 0; i < users.length; i++) {
                                users[i].id = i+1;
                               }
                               
                               $scope.users=users;
                        });


                    }]);