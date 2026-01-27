resource "aws_docdb_cluster" "docdb" {
  cluster_identifier      = "${var.project_name}-cluster"
  engine                  = "docdb"
  master_username         = "admin_user"
  master_password         = "password123"
  backup_retention_period = 5
  preferred_backup_window = "07:00-09:00"
  skip_final_snapshot     = true
  vpc_security_group_ids  = [aws_security_group.docdb_sg.id]
}

resource "aws_docdb_cluster_instance" "cluster_instances" {
  count              = 1
  identifier         = "${var.project_name}-instance"
  cluster_identifier = aws_docdb_cluster.docdb.id
  instance_class     = "db.t3.medium"
}