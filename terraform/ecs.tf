# sdk_test cluster
resource "aws_ecs_cluster" "sdk_test" {
  name = "sdk_test"
}

#
# task definition
#
resource "aws_ecs_task_definition" "nginx" {
  family                = "nginx"
  network_mode          = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu = 256
  memory = 512
  container_definitions = templatefile("${path.module}/container_definition.json", {})

  tags = {
    name = "sdk-test-nginx"
  }
}