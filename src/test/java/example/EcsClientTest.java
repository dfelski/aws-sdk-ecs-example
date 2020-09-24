package example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class EcsClientTest {

    private Ec2Client ec2Client;
    private EcsClient ecsClient;

    @BeforeEach
    void init(){

        ec2Client = Ec2Client.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        ecsClient = EcsClient.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        DescribeSubnetsResponse describeSubnetsResponse = ec2Client.describeSubnets(
                DescribeSubnetsRequest.builder()
                        .filters(Filter.builder().name("tag:Name").values("sdk_test_subnet1", "sdk_test_subnet2").build())
                .build());

        describeSubnetsResponse.subnets();

    }

    //@Test
    void createService(){

        // we have only one cluster...
        String clusterArn = ecsClient.listClusters().clusterArns().get(0);

        // filter subnets based on tag Name
        List<String> subNetIds = ec2Client.describeSubnets(
                DescribeSubnetsRequest.builder()
                        .filters(Filter.builder().name("tag:Name").values("sdk_test_subnet1", "sdk_test_subnet2").build())
                .build()).subnets().stream().map(s -> s.subnetId()).collect(Collectors.toList());

        ecsClient.createService(CreateServiceRequest.builder()
                .launchType(LaunchType.FARGATE)
                .desiredCount(1)
                .serviceName("nginxService")
                .taskDefinition("nginx")
                .networkConfiguration(NetworkConfiguration.builder()
                        .awsvpcConfiguration(AwsVpcConfiguration.builder()
                                .subnets(subNetIds)
                                .build()).build())
                .cluster(clusterArn).build());

        DescribeClustersResponse describeClustersResponse =
                ecsClient.describeClusters(
                        DescribeClustersRequest.builder()
                            .clusters(clusterArn).build());

    }

    //@Test
    void deleteService(){
        String clusterArn = ecsClient.listClusters().clusterArns().get(0);

        // scale to 0 first
        ecsClient.updateService(UpdateServiceRequest.builder().cluster(clusterArn).service("nginxService").desiredCount(0).build());

        // then delete
        ecsClient.deleteService(DeleteServiceRequest.builder().cluster(clusterArn).service("nginxService").build());
    }

}
