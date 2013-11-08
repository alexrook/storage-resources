'use strict';


// Declare app level module 
angular.module('sres', ['sres.controllers']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/bytypefiles',
                        {templateUrl: 'partials/reports/bytypefiles.html',
                         controller: 'ReportCtrl'});
    $routeProvider.when('/bigfiles',
                        {templateUrl: 'partials/reports/bigfiles.html',
                         controller: 'ReportCtrl'});
    $routeProvider.when('/users',
                        {templateUrl: 'partials/users.html', controller: 'UserCtrl'});
    $routeProvider.otherwise({redirectTo: '/users'});
  }]);
