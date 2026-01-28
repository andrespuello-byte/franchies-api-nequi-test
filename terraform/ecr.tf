resource "aws_ecr_repository" "api" {
  name         = var.project_name
  force_delete = true
}

resource "aws_ecr_repository_policy" "api_policy" {
  repository = aws_ecr_repository.api.name

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid    = "FullAccessForUser"
        Effect = "Allow"
        Principal = {
          AWS = "arn:aws:iam::008857349951:user/andres"
        }
        Action = "ecr:*"
      }
    ]
  })
}