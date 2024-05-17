var app = angular.module("app", []);
app.controller("ctrl", function($scope, $http, $window) {

	$http.get("/rest/authorities").then(resp => {
		$scope.db = resp.data;
		console.log(resp.data);
	})

	$scope.index_of = function(username, role) {
		return $scope.db.authorities
			.findIndex(a => a.user.username == username && a.role.id == role);

	}

	$http.get("/api/user").then(function(response) {
		var usenameFromServer = response.data.name;
		console.log("Usename from server>> ", usenameFromServer);

		$scope.update = function(username, role) {
			var index = $scope.index_of(username, role);
			console.log("Found user at index:", index);

			if (index >= 0) {
				console.log("Found user at index:", index);
				console.log("$scope.db.authorities[index]:", $scope.db.authorities[index]);
				console.log("Authorities data:", $scope.db.authorities);

				var id = $scope.db.authorities[index].id;
				if ($scope.db.authorities[index].user.username === usenameFromServer) {
					$window.swal({
						title: "Lỗi",
						text: "Không được xét quyền của chính mình!",
						type: "error",
					});
					$http.get("/rest/authorities").then(resp => {
						$scope.db = resp.data;
						console.log(resp.data);
					})
					return;
				}

				if ($scope.canUpdateRole(username)) {
					console.error("Cannot update your own role.");
					$window.swal({
						title: "Lỗi",
						text: "Không được xét quyền cho người có cùng quyền!",
						type: "error",
					});

					$http.get("/rest/authorities").then(resp => {
						$scope.db = resp.data;
						console.log(resp.data);
					})
					return;
				}
				$http.delete(`/rest/authorities/${id}`).then(resp => {
					console.log("Delete successful");
					$scope.db.authorities.splice(index, 1); //xóa trong list phía cleint
				})
			} else{
				
				var UserRole = {
					user: { username: username },
					role: { id: role }
				};
				$http.post('/rest/authorities/', UserRole).then(resp => {
					$scope.db.authorities.push(resp.data);
				});
			}
		}
	}).catch(function(error) {
		console.error("Error getting user information:", error);
	});

	$scope.canUpdateRole = function(username) {
		// Thực hiện kiểm tra xem người dùng có quyền nào và xác định liệu có thể xét quyền cho nhau không
		// Trong ví dụ này, giả sử chỉ có quyền 'admin' mới có thể xét quyền cho người khác
		var isAdmin = $scope.isAdmin(username);
		console.log("Calling isAdmin with:", username);
		console.log("isAdmin for admin user:", $scope.isAdmin(username));

		return isAdmin;
	};

	$scope.isAdmin = function(username) {
		var adminIndex = $scope.index_of(username, 'MANAGER');
		console.log("isAdmin:", adminIndex >= 0);
		return adminIndex >= 0;
	};
})