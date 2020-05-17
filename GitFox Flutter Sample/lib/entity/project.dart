/// Gitlab project entity
class Project {
  int id;
  String name;
  String avatarUrl;

  Project.fromJson(Map<String, dynamic> data) {
    id = data['id'];
    name = data['name'];
    avatarUrl = data['avatarUrl'];
  }

  @override
  String toString() {
    return "Project "
        "id: $id, "
        "name: $name, "
        "avatarUrl: $avatarUrl";
  }
}
