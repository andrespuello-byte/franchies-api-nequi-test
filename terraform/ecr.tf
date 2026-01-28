resource "aws_ecr_repository" "api" {
  name = var.project_name
  force_delete = true
}
