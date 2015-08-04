//6517719 zy17719 adrian hartanto

#include <stdio.h>
#include <ctype.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>
#define EARTH_RADIUS 6371

double toRadian(double point)
{
    return (point*M_PI)/180.0;
}

double toDegree(double point)
{
    return point*180.0/ M_PI;
}

int toBearing(double angle)
{
    
    double bearing = toDegree(angle)+ 360.0;
    return fmod(bearing, 360);
}

/*

	This function will determine the bearing of the path to be travelled

*/

double getBearing(double longtitude_start, double latitude_start, double longtitude_end, double latitude_end)
{
    double rad_latitude_start = toRadian(latitude_start);
    
    double rad_latitude_end = toRadian(latitude_end);
    
    double deltaLongtitude = toRadian(longtitude_end)- toRadian(longtitude_start);
    
    double x = sin(deltaLongtitude)*cos(rad_latitude_end);
    
    double y = cos(rad_latitude_start)*sin(rad_latitude_end)-sin(rad_latitude_start)*cos(rad_latitude_end)*cos(deltaLongtitude);
    
    return toBearing(atan2(x, y));
    
}

/*

	This function will determine the distance between
	two coordinates used for comparison later on.

*/

double getDistance(double longtitude_start, double latitude_start, double longtitude_end, double latitude_end)
{
    
    double rad_latitude_start = toRadian(latitude_start);
    
    double rad_latitude_end = toRadian(latitude_end);
    
    double deltaLongtitude = fabs(toRadian(longtitude_end)- toRadian(longtitude_start));
    
    double deltaLatitude = fabs(toRadian(latitude_end) - toRadian(latitude_start));
    
    double x = pow(sin(deltaLatitude/2),2);
    
    double y = cos(rad_latitude_start)*cos(rad_latitude_end)*pow(sin(deltaLongtitude/2),2);
    
    double c = 2 * atan2(sqrt(x+y),sqrt(1-(x+y)));
    
    return EARTH_RADIUS *c;
    
}

/* 

	The function below will determine the bus stop that is
 
	nearest from one point.

*/

char * toBikeStop(char *destination, double longtitude, double latitude, int file_size, char**stop_bus)
{
    double longtitude_bus[2];
    
    double latitude_bus[2];
    
    double distance[2];
    
    char *stop = NULL;
    
    char *stop1 = NULL;
    
    int num_of_characters = 0;
    
    int m =0;
    
    int x = 0;
    
    while(num_of_characters < strlen(destination)-1)
    {
         
        
        if(stop == NULL)
        {
            stop = malloc(45*sizeof(char));
            if(stop == NULL)
            {
  		printf("Memory not allocated for the bus stop\n");
            }
            
            while(destination[num_of_characters] != '\n')
            {
                stop[x] = destination[num_of_characters];
                x++;
                num_of_characters++;
            }
	    if(destination[num_of_characters] == '\n')
            {	
		num_of_characters++;
                stop[x] = '\0';
            }
            
        }
        
        if( stop1 == NULL)
        { 
            stop1 = malloc(45*sizeof(char));
            if(stop == NULL)
            {
  		printf("Memory not allocated for the bus stop\n");
            }
            while(destination[num_of_characters] != '\n')
            {
                stop1[m] = destination[num_of_characters];
                num_of_characters++;
                m++;
            }
            if(destination[num_of_characters] == '\n')
            {
		num_of_characters++;
                stop1[m] = '\0';
            }
            
        }

        sscanf(stop, "%lf %lf", &longtitude_bus[0], &latitude_bus[0]);
        distance[0] = getDistance(longtitude, latitude, longtitude_bus[0], latitude_bus[0]);
	
	        
        sscanf(stop1, "%lf %lf", &longtitude_bus[1], &latitude_bus[1]);
        distance[1] = getDistance(longtitude, latitude, longtitude_bus[1], latitude_bus[1]);
        
        
        if(distance[1] > distance[0])
        {
	    free(stop1);
            stop1 = NULL;
            *stop_bus = stop;
            
        }
        else if (distance[1] == distance[0])
        {
            if(longtitude_bus[1] > longtitude_bus[0])
            {
                free(stop);
                stop = NULL;
                *stop_bus = stop1;
                continue;
                
            }
            
            else if(longtitude_bus[1] == longtitude_bus[0])
            {
                
                if(latitude_bus[1] > latitude_bus[0])
                {
                    free(stop);
                    stop = NULL;
                    *stop_bus = stop1;
                     continue;
                }
                else
                {
                    free(stop1);
                    stop1 = NULL;
                    *stop_bus = stop;
                    continue;
                }
            }
            else
            {
		free(stop1);
                stop1 = NULL;
                *stop_bus = stop;
                continue;
            }
        }
        else
        {
            free(stop);
            stop = NULL;
            *stop_bus = stop1;
        }
        x = 0;
        m = 0;
        
}
	
    if(stop == NULL)
   {
	return stop;
   }
   if(stop1 == NULL)
   {
	return stop1;
    }
}

/*

	The function below will load the whole contents

	of the file to the memory by allocating the exact size

*/

long loadToMemory(const char*filename, char **destination)
{
    long size = 0;
    int space_counter = 0;
    char*bikestop = malloc(45 * sizeof(char));
    double langi;
    double lat;
    char string[40];

    FILE *fptr = fopen(filename, "r");

    if(fptr == NULL)
    {
        perror("Cannot access bike stop file: ");
        exit(0);
    }
    
    int i = 0;
    int m = 0;
    while(!feof(fptr))
   {    
    	char c = fgetc(fptr);

        while(c != '\n')
	{
	     if(c == ' ')
	     {		
		   space_counter++;
		   bikestop[i] = ' ';
	      	   if(space_counter < 2)
	      	   {
			while(bikestop[m] != bikestop[i])
			{
		    		if(isalpha(bikestop[m]) != 0)
		    		{	
					free(bikestop);
					printf("Invalid input from file\n");
					exit(1);	
		    		}
				m++;
			}
		    }

	    	   if(space_counter == 2)
		   {
			
		   	if(bikestop[m-1] == ' ')
			{	
				free(bikestop);
				printf("Invalid input from file\n");
				exit(1);
			}
			m++;
			while(bikestop[m] != bikestop[i])
			{
		    		if(isalpha(bikestop[m]) != 0)
		    		{	
					free(bikestop);
					printf("Invalid input from file\n");
					exit(1);	
		    		}
				m++;
			}
			
	           } 
	      }

	   while(c != ' ')
	   {
		bikestop[i] = c;
		break;
	   }
	    break;	
	}
	
	if(c == '\n')
	{	

	  	sscanf(bikestop, "%lf%lf", &langi, &lat);
    		
	  	if (-180 > langi || langi > 180)
	  	{
			free(bikestop);
			printf("Invalid input from file\n");
			exit(1);
          	}
          	if (-90 > lat || lat > 90)
	  	{
			free(bikestop);
			printf("Invalid input from file\n");
			exit(1);
          	}
		i=0;
		m=0;
		space_counter = 0;
		continue;
	}	
		i++;

   }	

    
	
    fseek(fptr,0,SEEK_END);

    size = ftell(fptr);

    fseek(fptr, 0, SEEK_SET);
    
    *destination = malloc(size);

    if(*destination == NULL)
    {
        printf("Memory not allocated for the file \n");
        exit(1);
    }
    
    fread(*destination,sizeof(char),size,fptr);
    
    fclose(fptr);
    
    return size;
}

/*
 
	This function will determine the direction that should be travelled

	e.g North/North East.

*/

void getDirection(int bearing, char**direction)
{
    int angle[10] = {0,22.5,67.5,112.5,157.5,202.5,247.5,292.5,337.5,360};
    char *name[9];
    
    name[0] = "NORTH";
    name[1] = "NORTH-EAST";
    name[2] = "EAST";
    name[3] = "SOUTH-EAST";
    name[4] = "SOUTH";
    name[5] = "SOUTH-WEST";
    name[6] = "WEST";
    name[7] = "NORTH-WEST";
    name[8] = "NORTH";
    
    int b;
    for(b = 0; b < 10; b++)
    {
        if(bearing >= angle[b] && bearing < angle[b+1] )
        {
            *direction = name[b];
        }
    }
    
}

/*

	This function will check for any invalid characters in

	the arguments passed. If any, the program will exit with 

	code 1. 

*/

void checkInputError(int argument, const char * argv[])
{
    int h;
   
    for( h=0; h < strlen(argv[argument]); h++)
   {
      if(argv[argument][h] == '.')
      {
        continue;
      } 
      else
      {
        if(isalpha(argv[argument][h]) != 0 )
        {
           printf("Usage: ./bikehelper start_long start_lat end_long end_lat (in degrees) \n");
           exit(1);
        }
      }
        
    }
}



/*

	On execution, the program will first check if the number of arguments passed

	is of the desired size.Then it will  check for any invalid characters(e.g alphabets) that are

	passed in the command line argument. After that, it will check whether the longtitude & latitude

	are within the desired range. If it's not it will exit with code 1.


	The program will proceed by finding the nearest bus stop from the starting address.

	Then the next section of the program will determine the bus stop nearest to 

	the end point. Then it will use the data retrieved in the first part and the

	second part to determine the direction e.g WEST/NORTH.


*/


int main(int argc, const char * argv[])
{
    
    
    if(argc < 5 || argc > 5)
    {
        printf("Usage: ./bikehelper start_long start_lat end_long end_lat (in degrees) \n");
        exit(1);
    }
     
    int h;

    for( h = 1; h < argc; h++)
    {
    	checkInputError(h, argv);
    }

    if(-180 > atof(argv[1]) || atof(argv[1]) > 180.0) 
    {
        printf("Longtitude between -180 and 180 only \n");
        exit(1);
    }
     
     if(-180 > atof(argv[3]) || atof(argv[3]) > 180.0) 
     {
	printf("Longtitude between -180 and 180 only \n");
        exit(1);	
     }
     
    if(-90 > atof(argv[2]) || atof(argv[2]) > 90 )
    {
        printf("Latitude between -90 and 90 only \n");
        exit(1);
    }
    
    if(-90 > atof(argv[4]) || atof(argv[4]) > 90)
    {
	 printf("Latitude between -90 and 90 only \n");
         exit(1);
    }

    char *first_bikestop;
    
    char *destination_file;
    
    int file_size = (int)loadToMemory("/usr/local/unnc/ae1prg/npb/stops.txt" , &destination_file);
    
    char *second_bikestop;
    
    double longtitude_bus[3];
    
    double latitude_bus[3];
    
    char busStop_name[3][40];
    
    char *direction[3];
    
    int bearing[3];
    
    printf("Starting from %s %s \n", argv[1], argv[2]);
    
    
    /* ---------------------------------- First part of the output ----------------------------------- */
    
    
    char *stop =  toBikeStop(destination_file, atof(argv[1]), atof(argv[2]), file_size, &first_bikestop);
    
    sscanf(first_bikestop, "%lf %lf %s", &longtitude_bus[0], &latitude_bus[0], busStop_name[0]);
    
    bearing[0] = getBearing(atof(argv[1]), atof(argv[2]), longtitude_bus[0], latitude_bus[0]);

    getDirection(bearing[0], &direction[0]);
    
    printf("Walk %s to %s at %f, %f \n", direction[0], busStop_name[0], longtitude_bus[0], latitude_bus[0]);

    
    
    /* ---------------------------------- Second part of the output ----------------------------------- */

    
    char * stop1 = toBikeStop(destination_file, atof(argv[3]), atof(argv[4]), file_size, &second_bikestop);
    
    sscanf(second_bikestop, "%lf %lf %s", &longtitude_bus[1], &latitude_bus[1], busStop_name[1]);
    
    bearing[1] = getBearing(longtitude_bus[0], latitude_bus[0], longtitude_bus[1], latitude_bus[1]);

    getDirection(bearing[1], &direction[1]);
    
    printf("Cycle %s to %s at %f, %f \n", direction[1], busStop_name[1], longtitude_bus[1],latitude_bus[1]);



    /* ---------------------------------- Third part of the output ----------------------------------- */
    
    bearing[2] = getBearing(longtitude_bus[1], latitude_bus[1], atof(argv[3]), atof(argv[4])),
    
    getDirection(bearing[2], &direction[2]);
    
    printf("Walk %s to your destination at %f, %f \n",direction[2],atof(argv[3]), atof(argv[4]));
    
    free(stop);
    free(stop1);
    free(destination_file);
    return 0;
    
}

