'use strict';


// Declare app level module 
angular.module('sres', ['sres.controllers','sres.services']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/reports',
                        {templateUrl: 'partials/reports/reports.html',
                         controller: 'ReportWrapCtrl'});
 
    $routeProvider.when('/users',
                        {templateUrl: 'partials/users.html', controller: 'UserCtrl'});
    
    $routeProvider.otherwise({redirectTo: '/users'});
  }]);
