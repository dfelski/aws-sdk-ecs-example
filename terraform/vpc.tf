# VPC definition
resource "aws_vpc" "sdk_test" {
  cidr_block           = var.cidr
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = {
    Name = "sdk_test"
  }
}
/*
# Internet Gateway
resource "aws_internet_gateway" "sdk_test" {
  vpc_id = aws_vpc.sdk_test.id

  tags = {
    Name = "sdk_test"
  }
}

#
# Default security group to allow internal ingress
#
resource "aws_default_security_group" "sdk_test" {
  vpc_id = aws_vpc.sdk_test.id

  ingress {
    from_port = 0
    to_port   = 0
    protocol  = -1
    cidr_blocks = ["10.0.0.0/16"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = -1
    cidr_blocks = ["0.0.0.0/0"]
  }
}

*/
####
# subnets definitions
####
resource "aws_subnet" "subnet1" {
  cidr_block              = var.subnets[0]
  availability_zone       = var.availability_zones[0]
  vpc_id                  = aws_vpc.sdk_test.id
  map_public_ip_on_launch = false

  tags = {
    Name = "sdk_test_subnet1"
  }
}

resource "aws_subnet" "subnet2" {
  cidr_block              = var.subnets[1]
  availability_zone       = var.availability_zones[1]
  vpc_id                  = aws_vpc.sdk_test.id
  map_public_ip_on_launch = false

  tags = {
    Name = "sdk_test_subnet2"
  }
}