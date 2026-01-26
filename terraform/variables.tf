variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "vpc_id" {
  description = "VPC donde se despliega la infraestructura"
  type        = string
}

variable "project_name" {
  type    = string
  default = "franchises-api"
}

variable "public_subnets" {
  description = "Subnets públicas para el ALB"
  type        = list(string)
}
