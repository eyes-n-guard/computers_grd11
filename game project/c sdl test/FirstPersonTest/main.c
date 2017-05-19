#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <SDL.h>
#include <math.h>

#define WIDTH 1600
#define HEIGHT 900
#define SENSITIVITY 2

#define MAP_FILE "complexRoom.txt"

#define BACKFACE_CULL_WIREFRAME false

const float FRUSTUM_WIDTH = 1;

int sign(float a);
float min(float a, float b);
float max(float a, float b);
float clamp(float a, float bot, float top);



typedef struct //vec3
{
    float x,y,z;
} vec3;

typedef struct //camera
{
    vec3 pos, vel;
    float pitch, yaw, speed, accel, decel; //angle and yaw in radians
} camera;

typedef struct //face //each face is a triangle, with 3 indexes in the vertex array
{
    int p1, p2, p3, texture, type;//type: 0: normal face, 1: areaportal
    vec3 norm;
} face;

float length(vec3 a);
float dot(vec3 a, vec3 b);

vec3 add(vec3 a, vec3 b);
vec3 sub(vec3 a, vec3 b);
vec3 mul(vec3 a, float b);
vec3 unit(vec3 a);
vec3 cross(vec3 a, vec3 b);
vec3 rotate(vec3 a, vec3 b, float theta);
vec3 rotateZ(vec3 a, float theta);
vec3 rotateX(vec3 a, float theta);
vec3 dropX(vec3 a);
vec3 dropY(vec3 a);
vec3 dropZ(vec3 a);

void intersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float *xOut, float *yOut);

void drawLine(vec3 p1, vec3 p2, SDL_Renderer *render);
void drawWireframeFace(face f, camera player, vec3 *points, SDL_Renderer *render);
void loadMap(int *nVectors, int *nFaces, vec3 **vectors, face **faces, char *fileName);

int main(int argc, char **argv)
{
    SDL_Window *window = NULL;
    SDL_Renderer *renderer = NULL;

    if(SDL_Init(SDL_INIT_VIDEO) != 0)
        return 1;

    window = SDL_CreateWindow("Dank meme", SDL_WINDOWPOS_UNDEFINED, SDL_WINDOWPOS_UNDEFINED, WIDTH, HEIGHT, SDL_WINDOW_SHOWN);
    renderer = SDL_CreateRenderer(window,-1,SDL_RENDERER_ACCELERATED);

    SDL_CaptureMouse(true);
    SDL_SetRelativeMouseMode(true);

    //set up map
    int mapVectorsNum = 0;
    int mapFacesNum = 0;
    vec3 *mapVectors = NULL;
    face *mapFaces = NULL;
    loadMap(&mapVectorsNum, &mapFacesNum, &mapVectors, &mapFaces, MAP_FILE);

    camera player = {.pos = {.x = 0, .y = 0, .z = -400}, .vel = {.x = 0, .y = 0, .z = 0}, .pitch = 0, .yaw = 0, .speed = 15, .accel = 1.2, .decel = 1.2};

    bool quit = false;
    SDL_Event e;
    int arrows = 0; //up: 1, left: 2, down: 4, right: 8
    int wasd = 0; //w: 1, a: 2, s: 4, d: 8
    while(!quit)
    {
        while(SDL_PollEvent(&e))
        {
            if(e.type == SDL_QUIT)
                quit = true;
            else if(e.type == SDL_KEYDOWN) //wasd keycode thing
            {
                switch(e.key.keysym.sym)
                {
                    case SDLK_ESCAPE:
                        quit = true;
                    break;

                    case SDLK_w:
                        wasd |= 1;
                    break;

                    case SDLK_a:
                        wasd |= 2;
                    break;

                    case SDLK_s:
                        wasd |= 4;
                    break;

                    case SDLK_d:
                        wasd |= 8;
                    break;


                    case SDLK_UP:
                        arrows |= 1;
                    break;

                    case SDLK_LEFT:
                        arrows |= 2;
                    break;

                    case SDLK_DOWN:
                        arrows |= 4;
                    break;

                    case SDLK_RIGHT:
                        arrows |= 8;
                    break;
                }
            }
            else if(e.type == SDL_KEYUP)
            {
                switch(e.key.keysym.sym)
                {
                    case SDLK_w:
                        wasd &= 14;
                    break;

                    case SDLK_a:
                        wasd &= 13;
                    break;

                    case SDLK_s:
                        wasd &= 11;
                    break;

                    case SDLK_d:
                        wasd &= 7;
                    break;


                    case SDLK_UP:
                        arrows &= 14;
                    break;

                    case SDLK_LEFT:
                        arrows &= 13;
                    break;

                    case SDLK_DOWN:
                        arrows &= 11;
                    break;

                    case SDLK_RIGHT:
                        arrows &= 7;
                    break;
                }
            }
            else if(e.type == SDL_MOUSEMOTION)
            {
                player.yaw -= e.motion.xrel*0.0004*SENSITIVITY;
                player.pitch = clamp(player.pitch + e.motion.yrel*0.0004*SENSITIVITY, -M_PI/2, M_PI/2);
            }
        }

        //player movement & linear interpolation
        vec3 dv = {.x = (((wasd & 8) >> 3) - ((wasd & 2) >> 1)), .y = ((wasd & 1) - ((wasd & 4) >> 2)), .z = 0};
        dv = mul(unit(dv),player.accel);

        //player.yaw -= (((arrows & 8) >> 3) - ((arrows & 2) >> 1)) * 0.02;
        //player.pitch = clamp(player.pitch + (((arrows & 4) >> 2) - (arrows & 1)) * 0.02,-M_PI/2, M_PI/2);

        float zHold = player.vel.z; //zero z value of velocity vector so movement is only applied to x and y axes
        player.vel.z = 0;

        player.vel = rotateZ(player.vel, -player.yaw);
        if(dv.x == 0)//decelerate player based on released keys and camera yaw
        {
            if(player.vel.x > 0)
                player.vel.x = max(player.vel.x - player.decel, 0);
            else
                player.vel.x = min(player.vel.x + player.decel, 0);
        }

        if(dv.y == 0)
        {
            if(player.vel.y > 0)
                player.vel.y = max(player.vel.y - player.decel, 0);
            else
                player.vel.y = min(player.vel.y + player.decel, 0);
        }

        player.vel = add(player.vel, dv);
        player.vel = rotateZ(player.vel, player.yaw);

        if(length(player.vel) > player.speed) //limit speed
            player.vel = mul(unit(player.vel),max(length(player.vel) - player.decel, player.speed));
            //player.vel = mul(unit(player.vel), player.speed);
        player.vel.z = zHold;

        player.pos = add(player.pos, player.vel); //move player based on velocity



        //printf("x: %0.2f y: %0.2f z: %0.2f  speed: %0.2f\n", player.pos.x, player.pos.y, player.pos.z, length(player.vel));

        SDL_SetRenderDrawColor(renderer, 255,255,255, SDL_ALPHA_OPAQUE);
        SDL_RenderClear(renderer);

        SDL_SetRenderDrawColor(renderer, 0,200,200, SDL_ALPHA_OPAQUE);
        int faceIndex;
        for(faceIndex=0;faceIndex < mapFacesNum;faceIndex++)
            drawWireframeFace(mapFaces[faceIndex], player, mapVectors, renderer);

        SDL_RenderPresent(renderer);
        SDL_Delay(16);
    }
    if (renderer)
        SDL_DestroyRenderer(renderer);

    if (window)
        SDL_DestroyWindow(window);


    free(mapVectors);
    free(mapFaces);
    SDL_DestroyWindow(window);
    SDL_Quit();

    return 0;
}

/*
map file format:
num_of_vertices,num_of_faces
vec0.x,vec0.y,vec0.z
vec1.x,vec1.y,vec1.z
vec2.x,vec2.y,vec2.z
...
face0.p1,face0.p2,face0.p3,face0.texture,face0.norm.x,face0.norm.y,face0.norm.z,face0.type
face1.p1,face1.p2,face1.p3,face1.texture,face1.norm.x,face1.norm.y,face1.norm.z,face1.type
...
*/

void loadMap(int *nVectors, int *nFaces, vec3 **vectors, face **faces, char *fileName)
{
    FILE *mapFile = fopen(fileName, "r");
    fscanf(mapFile,"%d,%d\n",nVectors,nFaces);
    *vectors = (vec3 *)malloc(*nVectors * sizeof(vec3));
    *faces = (face *)malloc(*nFaces * sizeof(face));

    int i;
    for(i=0;i < *nVectors;i++)
        fscanf(mapFile,"%f,%f,%f\n",&(*vectors)[i].x, &(*vectors)[i].y, &(*vectors)[i].z);
    for(i=0;i < *nFaces;i++)
        fscanf(mapFile,"%d,%d,%d,%d,%f,%f,%f,%d\n", &(*faces)[i].p1, &(*faces)[i].p2, &(*faces)[i].p3, &(*faces)[i].texture, &(*faces)[i].norm.x, &(*faces)[i].norm.y, &(*faces)[i].norm.z, &(*faces)[i].type);
    fclose(mapFile);
}

void drawWireframeFace(face f, camera player, vec3 *points, SDL_Renderer *render)
{
    if(sub(f.norm,player.pos).y >= 0 || !BACKFACE_CULL_WIREFRAME) //only draw if the face is facing towards the player, if backface culling is enabled
    {
        vec3 p1R = rotateX(rotateZ(sub(points[f.p1], player.pos), -player.yaw), -player.pitch); //rotate and translate points relative to player
        vec3 p2R = rotateX(rotateZ(sub(points[f.p2], player.pos), -player.yaw), -player.pitch);
        vec3 p3R = rotateX(rotateZ(sub(points[f.p3], player.pos), -player.yaw), -player.pitch);

        drawLine(p1R,p2R,render);
        drawLine(p2R,p3R,render);
        drawLine(p3R,p1R,render);
    }
}

void drawLine(vec3 p1, vec3 p2, SDL_Renderer *render)
{
    vec3 start = {p1.x, p1.y, p1.z};
    vec3 end = {p2.x, p2.y, p2.z};
    vec3 disp = sub(end, start);

    if(start.y >= 0 || end.y >= 0)
    {
        vec3 left = {-FRUSTUM_WIDTH, 1, 0}; //normals of the planes which make up the view frustum (point inwards)
        left = unit(left);
        vec3 right = {FRUSTUM_WIDTH, 1, 0};
        right = unit(right);
        vec3 up = {0, 1, FRUSTUM_WIDTH};
        up = unit(up);
        vec3 down = {0, 1, -FRUSTUM_WIDTH};
        down = unit(down);
        vec3 forward = {0,1,0};


        if(start.y < 0)
        {
            start = add(start, mul(disp, dot(mul(start, -1), forward)/dot(disp,forward)));
            //start.y += 0.01;
        }

        if(end.y < 0)
        {
            end = add(start, mul(disp, dot(mul(start, -1), forward)/dot(disp,forward)));
            //end.y += 0.01;
        }

        int code1 = 0, code2 = 0;
        if(dot(start, up) < 0)
            code1 |= 1;
        if(dot(start, down) < 0)
            code1 |= 2;
        if(dot(start, right) < 0)
            code1 |= 4;
        if(dot(start, left) < 0)
            code1 |= 8;

        if(dot(end, up) < 0)
            code2 |= 1;
        if(dot(end, down) < 0)
            code2 |= 2;
        if(dot(end, right) < 0)
            code2 |= 4;
        if(dot(end, left) < 0)
            code2 |= 8;

        if((code1 & code2) == 0)
        {
            if((code1 & 1) == 1) //start should be clipped to top plane
                start = add(start, mul(disp, dot(mul(start,-1),up)/dot(disp,up)));
            if((code1 & 2) == 2) //start should be clipped to bottom plane
                start = add(start, mul(disp, dot(mul(start,-1),down)/dot(disp,down)));
            if((code1 & 4) == 4) //start should be clipped to right plane
                start = add(start, mul(disp, dot(mul(start,-1),right)/dot(disp,right)));
            if((code1 & 8) == 8) //start should be clipped to left plane
                start = add(start, mul(disp, dot(mul(start,-1),left)/dot(disp,left)));


            if((code2 & 1) == 1) //end should be clipped to top plane
                end = add(start, mul(disp, dot(mul(start,-1),up)/dot(disp,up)));
            if((code2 & 2) == 2) //end should be clipped to bottom plane
                end = add(start, mul(disp, dot(mul(start,-1),down)/dot(disp,down)));
            if((code2 & 4) == 4) //end should be clipped to right plane
                end = add(start, mul(disp, dot(mul(start,-1),right)/dot(disp,right)));
            if((code2 & 8) == 8) //end should be clipped to left plane
                end = add(start, mul(disp, dot(mul(start,-1),left)/dot(disp,left)));

            //3d projection transformation
            start.z *= (HEIGHT/2) / start.y;
            start.x *= (WIDTH/2)  / start.y;

            end.z *= (HEIGHT/2) / end.y;
            end.x *= (WIDTH/2)  / end.y;

            //printf("%f %f   %f %f", start.x, start.z, end.x, end.z);
            SDL_RenderDrawLine(render,start.x + WIDTH/2, start.z + HEIGHT/2, end.x + WIDTH/2, end.z + HEIGHT/2);
        }
    }

}

int sign(float a)
{
    if(a > 0)
        return 1;
    if(a < 0)
        return -1;
    return 0;
}

float min(float a, float b)
{
    return a < b ? a : b;
}

float max(float a, float b)
{
    return a > b ? a : b;
}

float clamp(float a, float bot, float top)
{
    return min(max(bot, a), top);
}

float length(vec3 a)
{
    return sqrt((double)((a.x * a.x) + (a.y * a.y) + (a.z * a.z)));
}

float dot(vec3 a, vec3 b)
{
    return (a.x * b.x) + (a.y * b.y) + (a.z * b.z);
}

vec3 add(vec3 a, vec3 b)
{
    vec3 r = {.x = a.x + b.x, .y = a.y + b.y, .z = a.z + b.z};
    return r;
}

vec3 sub(vec3 a, vec3 b)
{
    vec3 r = {.x = a.x - b.x, .y = a.y - b.y, .z = a.z - b.z};
    return r;
}

vec3 mul(vec3 a, float b)
{
    vec3 r = {.x = a.x * b, .y = a.y * b, .z = a.z * b};
    return r;
}

vec3 unit(vec3 a)
{
    float len = length(a);
    vec3 r = mul(a,(len != 0 ? 1/len : 0));//check for vector length of 0 to prevent divide by zero error
    return r;
}

vec3 cross(vec3 a, vec3 b)
{
    vec3 r = {.x = (a.y * b.z) - (a.z * b.y), .y = (a.z * b.x) - (a.x * b.z), .z = (a.x * b.y) - (a.y * b.x)};
    return r;
}

vec3 rotate(vec3 a, vec3 b, float theta) //rotates vector a theta radians around axis b //TODO: implement the more efficient way of doing this
{
    vec3 parallel = mul(unit(a),dot(a,unit(b)));
    vec3 perpendicular = sub(a,parallel);
    vec3 r = add(mul(add(mul(unit(perpendicular),cos(theta)),mul(unit(cross(b,perpendicular)),sin(theta))),length(perpendicular)),parallel);
    return r;
}

vec3 rotateZ(vec3 a, float theta) //faster version of rotate with z as the axis
{
    float sinTheta = sin(theta);
    float cosTheta = cos(theta);

    vec3 r = {.x = a.x * cosTheta - a.y * sinTheta, .y = a.y * cosTheta + a.x * sinTheta, .z = a.z};
    return r;
}

vec3 rotateX(vec3 a, float theta)
{
    float sinTheta = sin(theta);
    float cosTheta = cos(theta);

    vec3 r = {.x = a.x, .y = a.y * cosTheta - a.z * sinTheta, .z = a.z * cosTheta + a.y * sinTheta};
    return r;
}

vec3 dropX(vec3 a)
{
    vec3 r = {.x = 0, .y = a.y, .z = a.z};
    return r;
}

vec3 dropY(vec3 a)
{
    vec3 r = {.x = a.x, .y = 0, .z = a.z};
    return r;
}

vec3 dropZ(vec3 a)
{
    vec3 r = {.x = a.x, .y = a.y, .z = 0};
    return r;
}

void intersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float *xOut, float *yOut) //idek
{
    float dx12 = x1-x2;
    float dx34 = x3-x4;
    float dy12 = y1-y2;
    float dy34 = y3-y4;
    float a = x1*y2 - y1*x2;
    float b = x3*y4 - y3*x4;
    float base = dx12*dy34 - dy12*dx34;

    *xOut = (a*dx34 - b*dx12) / base;
    *yOut = (a*dy34 - b*dy12) / base;

}
