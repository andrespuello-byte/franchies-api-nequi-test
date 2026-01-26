resource "aws_ecs_cluster" "cluster" {
  name = "${var.project_name}-cluster"
}

resource "aws_ecs_task_definition" "task" {
  family                   = var.project_name
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 256
  memory                   = 512
  execution_role_arn       = aws_iam_role.ecs_task_role.arn

  container_definitions = jsonencode([
    {
      name      = var.project_name
      image     = "${aws_ecr_repository.api.repository_url}:latest"
      essential = true
      portMappings = [{
        containerPort = 8080
      }]
      environment = [
        {
          name  = "SPRING_PROFILES_ACTIVE"
          value = "prod"
        }
      ]
    }
  ])
}
