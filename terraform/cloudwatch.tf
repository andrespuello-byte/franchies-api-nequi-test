resource "aws_cloudwatch_log_group" "franchise_api_logs" {
  name              = "/ecs/franchise-api" # DEBE ser idéntico al de la task_definition
  retention_in_days = 7                    # Para que no te cobren de más guardando logs viejos

  tags = {
    Environment = "production"
    Application = "franchise-api"
  }
}