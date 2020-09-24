variable "region" {
  description = "AWS region"
  default     = "ap-south-1"
}

variable "availability_zones" {
  description = "availability zone"
  default = ["ap-south-1a", "ap-south-1b"]
}

variable "cidr" {
  description = "Network CIDR"
  default     = "10.0.0.0/16"

}

variable "subnets" {
  description = "Subnets"
  default     = ["10.0.1.0/24", "10.0.2.0/24"]
}