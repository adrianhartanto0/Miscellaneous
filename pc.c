#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <semaphore.h>
#include <time.h>

sem_t m;
sem_t t;
sem_t consumer;

#define NUM_OF_FILE 20

#define BUFFER_SIZE 15
int fileTable[NUM_OF_FILE][3];
char memory[BUFFER_SIZE];

// Generate random number
int returnRandomRumber(int min, int max){
	int randIndex = rand() % (max+1-min)+min;
	return randIndex;
}

// Generate random string
void returnRandomString(int size, char*s){
    	
    char charset[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    int i;
    for(i = 0; i < size;i++){
		size_t index = (double) rand() / RAND_MAX *(sizeof charset -1);
		*(s+i) = charset[index];
	}
}

// Find empty location in the memory
// for the length of the file. If none found return -1, else return the index
// of the location to put the file marked by indexPointer

int findEmptyLocation(int rand1){
	
	int indexPointer = 0;
	int foundLocation = 0; // 0 for yes 1 for no
		
	while(foundLocation != 1){
			
		for(; indexPointer < BUFFER_SIZE;indexPointer++){
			if(memory[indexPointer] != '\0' && memory[indexPointer] == '_'){
				break;
			}
		}
			
	    int indexLast = (indexPointer + rand1);


	    if(indexLast > BUFFER_SIZE- 1){
	    	foundLocation = 1;
	    }else{
	    	int y;
	    	int isfree = 1;
	    	for(y = indexPointer;y <= indexLast;y++){
	    	    if(memory[y] != '_'){
	    		isfree = 0;
	    		break;
	    	    }
	    	}

	    	if(isfree == 0){
	    	    if(indexLast == BUFFER_SIZE- 1){
	    		foundLocation = 1;
	    	    }else if(indexLast < (BUFFER_SIZE - 1)){
	    	        indexPointer = indexLast;
	    	    }
	    	    continue;
	    	}else{
	    		return indexPointer;
	    	}
	    }
	}
	
	return -1;
	
}

//check if the memory has hash
// 1 for has hash and 0 for no hash

int HasHash(){
	
	int t;
	for(t = 0; t < NUM_OF_FILE;t++){
		if(fileTable[t][2] != -1 && memory[fileTable[t][2]] == '#'){
			return 1;
		}	
	}
	return 0;
}

//check if the number negative item in the filetable 
//is equal to NUM_OF_FILE - 1. if filetable == (NUM_OF_FILE -1),
//No string left in the memory but hash only.

int checkIfNegative(){
	
	int t = 0;
	int counter = 0;
	
	for(t = 0; t < NUM_OF_FILE;t++){
		if(fileTable[t][2] <  0){
			counter++;
		}	
	}
	return (counter == NUM_OF_FILE-1);
}



//insert metadata to filetable
int insertToFileTable(int id, int size,int start){

	fileTable[id][0] = id;
	fileTable[id][1] = size;
	fileTable[id][2] = start;
}

//print contents of the memory
void printMemory(){
	
	printf("Memory content is: ");
	
	int y;
	for(y = 0;y < BUFFER_SIZE;y++){
	    if(memory[y] == '\0'){
		printf("_");    
	    }else{
		printf("%c",memory[y]);	 
	    }
		   
	}
	printf("\n");
}

//producer routine;

void *producer(){
	
	int counter = 0;
	int hasEnoughSpace = 0;
	
	
	while(counter < NUM_OF_FILE){
		
		int rand = returnRandomRumber(1,9);
		printf("Producer intended string length is %d\n",rand+1);
		
		if(rand > BUFFER_SIZE){
		    break;
		}
		
		int location = findEmptyLocation(rand);
	      
	        while(location == -1){
		    //printf("calling consumer\n");
		    sem_post(&consumer); // need to call consumer first	
		    sem_wait(&t);
		    location = findEmptyLocation(rand);
		}
		
		char *s = (char*)malloc(rand*sizeof(char));
		returnRandomString(rand,s);
		
		// inserting file to memory;		
  
		int i;
		for(i = 0; i < rand;i++){
			memory[i+location] = *(s+i);
		}
		
		printf("Produced string is %s\n",s);
		memory[i+location] = '\0';

		insertToFileTable(counter,rand+1,location);
		counter++;
		
		printMemory();
	   }

	   // check if has space for hash
	   int indexHash = findEmptyLocation(1);
	   while(indexHash == -1){
		//printf("calling consumer because no space for hash\n");
		sem_post(&consumer);
		sem_wait(&t);
		indexHash = findEmptyLocation(1);
	   }
	   
	   // insert hash to memory
	   memory[indexHash] = '#';
	   memory[indexHash+1] =  '\0';
          
           // insert hash metadata to file table

	   int o;
	   for(o = 0; o < NUM_OF_FILE;o++){
		if(fileTable[o][2] == -1){
   		    insertToFileTable(o,2,indexHash);
		    break;
		}
	    }

	   printf("Hash inserted at index %d\n",indexHash);
	   printMemory();
	   sem_post(&consumer);
 
		
	   return 0;
}

void *consumerRoutine(void *id){
	
	while(1){	

		sem_wait(&consumer);
		sem_wait(&m);
		
		int indexHash = -1;
		int hasRemovedPositive = 0;
		

		int x = returnRandomRumber(1,(int)NUM_OF_FILE);
		
		// if a particular consumer encounters hash two times,
                // it goes back to the top of the while loop
	  
	        if(indexHash != -1 && indexHash == x){
		   continue;
		}
		  
		if(fileTable[x-1][2] != -1){
			int fileSize = fileTable[x-1][1];
			int startIndex = fileTable[x-1][2];
			
			if(memory[startIndex] != '#'){
			   // A particular consumer consuming
			   printf("Consumer %d executing and string is:", *((int *)id));
			   int i;
			   for(i = 0; i < fileSize; i++){
				char c = memory[i+startIndex];
				printf("%c",c);
				memory[i+startIndex] = '_';	
			   }
			   fileTable[x-1][2] = -1;
			   printf("\n");
			}else{
			    printf("Consumer found %d, %c\n",*((int *)id),memory[startIndex]);
			    indexHash = x;
		      	}
			printMemory();
		}
		
		// check if memory has hash. If it doesn't (index == 0) notify producer,
                // if it has hash and there is still file in memory, notify other consumers;
		int index = HasHash();

		if(index == 0){
			sem_post(&t);
			sem_post(&m);
			hasRemovedPositive = 1;
		}else{
		   int y = checkIfNegative();
		   sem_post(&consumer);
		   sem_post(&m);
		   if(y == 1){
		    printf("Consumer%d quitting\n", *((int *)id));
		    return 0;
		   }	
		}

	  }
} 

int main(){
	
	// empty entry in the filetable
	int i;
	for(i = 0;i< (int)NUM_OF_FILE;i++){
		fileTable[i][2] = -1;
	}
	
	// empty file marker
	for(i = 0;i <BUFFER_SIZE;i++){
		memory[i] = '_';
	}
	
	sem_init(&consumer,0,0);
	sem_init(&t,0,0);
	sem_init(&m,0,1);
	
   pthread_t producer1;
   if(pthread_create(&producer1,NULL,producer,NULL) == -1){
	    printf("unable to create thread");
	    exit(0);   
   }
   
   int id=1;
   
   pthread_t consumer2;
   if(pthread_create(&consumer2,NULL,consumerRoutine,(void*) &id) == -1){
	    printf("unable to create thread");
	    exit(0);   
   }

   int id1 = 2;
   pthread_t consumer3;
   if(pthread_create(&consumer3,NULL,consumerRoutine,&id1) == -1){
	    printf("unable to create thread");
	    exit(0);   
   }
 /*int id2 = 3;
   pthread_t consumer4;
   if(pthread_create(&consumer4,NULL,consumerRoutine,&id2) == -1){
	    printf("unable to create thread");
	    exit(0);   
   }

     int id3 = 4;
   pthread_t consumer5;
   if(pthread_create(&consumer5,NULL,consumerRoutine,&id3) == -1){
	    printf("unable to create thread");
	    exit(0);   
   }*/
   
   pthread_join(producer1,NULL);
   pthread_join(consumer2,NULL); 
   pthread_join(consumer3,NULL);

   
	/*pthread_join(consumer4,NULL);
	pthread_join(consumer5,NULL);*/
}
