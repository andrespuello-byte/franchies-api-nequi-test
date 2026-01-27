output "alb_dns_name" {
  value = aws_lb.alb.dns_name
  description = "URL para acceder a la API"
}

output "ecr_repository_url" {
  value = aws_ecr_repository.api.repository_url
}