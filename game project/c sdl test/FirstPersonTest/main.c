#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <SDL.h>
#include <math.h>
#include <time.h>

static int WIDTH = 1920; //640
static int HEIGHT = 1080; //360
static float SENSITIVITY = 2.5;

#define CROSSHAIR_SIZE 10 //distance from edge of crosshair to center
#define CROSSHAIR_R 10
#define CROSSHAIR_G 255
#define CROSSHAIR_B 50

#define SETTINGS_FILE "settings.txt"
static char MAP_FILE[30];

#define BACKFACE_CULL_WIREFRAME true
#define BACKFACE_CULL_FILL true
#define GENERATE_FACE_NORMALS true

static float FRUSTUM_WIDTH = 1.0; // = 1/tan(fov/2)
static float FRUSTUM_NEAR_LENGTH = 0.01;
static int DRAW_EDGES = false;



int sign(float a);
float min(float a, float b);
float max(float a, float b);
float clamp(float a, float bot, float top);



typedef struct //vec3
{
    float x,y,z;
} vec3;

typedef struct //vec2
{
    float x,y;
} vec2;

typedef struct //camera
{
    vec3 pos, vel;
    float pitch, yaw, speed, accel, decel; //angle and yaw in radians
} camera;

typedef struct //face //each face is a triangle, with 3 indexes in the vertex array
{
    int p1, p2, p3, texture, type;//type is not being used
    vec3 norm, mid;
} face;

typedef struct //colour
{
    int r,g,b;
} colour;

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

vec2 add2(vec2 a, vec2 b);
vec2 sub2(vec2 a, vec2 b);
vec2 mul2(vec2 a, float b);
float length2(vec2 a);
float dot2(vec2 a, vec2 b);
vec2 unit2(vec2 a);
vec3 toVec3(vec2 a, float z);
vec2 perspective2d(vec3 a);

bool intersection(vec2 a1, vec2 a2, vec2 b1, vec2 b2, vec2 *result);

void loadConstants(int *w, int *h, float *sens, char *map, float *frustumW, float *frustumN, int *edges, char* fileName);
void quicksort(int list[], float ref[], int l, int r);
void drawFilledFaces(face *faces, int nFaces, camera player, vec3 *points, SDL_Renderer *render, colour *colours);
void drawLine(vec3 p1, vec3 p2, SDL_Renderer *render);
void drawWireframeFace(face f, camera player, vec3 *points, SDL_Renderer *render);
void loadMap(int *nVectors, int *nFaces, int *nColours, vec3 **vectors, face **faces, colour **colours, char *fileName, camera *player);
void transformFace(face f, vec3 *points, camera player, SDL_Renderer *renderer);
void drawWireframePolygon(vec2 *polygon, int nPoints, SDL_Renderer *renderer);
void fillTriangle(vec2 p1, vec2 p2, vec2 p3, SDL_Renderer *renderer);
void swapVec2Ptr(vec2 **p1, vec2 **p2);
int getClipCode(vec2 a);
void drawClippedLine(vec2 a, vec2 b, SDL_Renderer *renderer);

int main(int argc, char **argv)
{
    SDL_Window *window = NULL;
    SDL_Renderer *renderer = NULL;

    if(SDL_Init(SDL_INIT_VIDEO) != 0)
        return 1;

    loadConstants(&WIDTH, &HEIGHT, &SENSITIVITY, MAP_FILE, &FRUSTUM_WIDTH, &FRUSTUM_NEAR_LENGTH, &DRAW_EDGES, SETTINGS_FILE);

    window = SDL_CreateWindow("Dank meme", SDL_WINDOWPOS_UNDEFINED, SDL_WINDOWPOS_UNDEFINED, WIDTH, HEIGHT, SDL_WINDOW_SHOWN);
    renderer = SDL_CreateRenderer(window,-1, SDL_RENDERER_SOFTWARE);

    SDL_CaptureMouse(true);
    SDL_SetRelativeMouseMode(true);

    //set up map
    camera player;
    int mapVectorsNum = 0;
    int mapFacesNum = 0;
    int mapColoursNum = 0;
    vec3 *mapVectors = NULL;
    face *mapFaces = NULL;
    colour *mapColours = NULL;
    loadMap(&mapVectorsNum, &mapFacesNum, &mapColoursNum, &mapVectors, &mapFaces, &mapColours, MAP_FILE, &player);

    int lastTime;
    SDL_RendererInfo info;
    SDL_GetRendererInfo(renderer, &info);
    printf("%s\n", info.name);

    bool quit = false;
    SDL_Event e;
    int arrows = 0; //up: 1, left: 2, down: 4, right: 8
    int wasd = 0; //w: 1, a: 2, s: 4, d: 8, space: 16
    while(!quit)
    {
        lastTime = SDL_GetTicks();
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

                    case SDLK_SPACE:
                        wasd |= 16;
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
                        wasd &= ~1;
                    break;

                    case SDLK_a:
                        wasd &= ~2;
                    break;

                    case SDLK_s:
                        wasd &= ~4;
                    break;

                    case SDLK_d:
                        wasd &= ~8;
                    break;

                    case SDLK_SPACE:
                        wasd &= ~16;
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
                player.yaw -= e.motion.xrel*0.0004*SENSITIVITY; //change player's yaw and pitch angles based on the change in mouse position
                player.pitch = clamp(player.pitch + e.motion.yrel*0.0004*SENSITIVITY, -M_PI/2, M_PI/2); //keep pitch within -2*pi and 2*pi
            }
        }

        //FRUSTUM_WIDTH += (float)((arrows & 1) - ((arrows & 4) >> 2)) * 0.01;

        if(player.yaw > 2*M_PI) //keep yaw within the bounds of 0 and 2*pi
            player.yaw -= 2*M_PI;
        else if(player.yaw < 0)
            player.yaw += 2*M_PI;

        //player movement & linear interpolation
        vec3 dv = {.x = (((wasd & 8) >> 3) - ((wasd & 2) >> 1)), .y = ((wasd & 1) - ((wasd & 4) >> 2)), .z = 0};
        dv = mul(unit(dv),player.accel);

        //player.vel.z = (((arrows & 4) >> 2) - (arrows & 1)) * player.speed;

        //janky temporary jumping code
        player.vel.z += 1; //gravity
        if(player.pos.z >= -400) //move player out of ground
        {
            player.pos.z = -400;
            player.vel.z = 0;
        }

        if(player.pos.z == -400 && ((wasd & 16) == 16)) //if jump is pressed and at z == 0 then jump
            player.vel.z = -20;

        float zHold = player.vel.z; //zero z value of velocity vector so movement is only applied to x and y axes
        player.vel.z = 0;

        //FRUSTUM_WIDTH = clamp(length(player.vel) * 2 / player.speed, 0.01, 2);

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

        SDL_SetRenderDrawColor(renderer, 128,128,128, SDL_ALPHA_OPAQUE);
        SDL_RenderClear(renderer);

        drawFilledFaces(mapFaces, mapFacesNum, player, mapVectors, renderer, mapColours);

        //draw crosshair
        SDL_SetRenderDrawColor(renderer, CROSSHAIR_R, CROSSHAIR_G, CROSSHAIR_B, SDL_ALPHA_OPAQUE);
        SDL_RenderDrawLine(renderer, WIDTH/2 + CROSSHAIR_SIZE, HEIGHT/2, WIDTH/2 - CROSSHAIR_SIZE, HEIGHT/2);
        SDL_RenderDrawLine(renderer, WIDTH/2, HEIGHT/2 + CROSSHAIR_SIZE, WIDTH/2, HEIGHT/2 - CROSSHAIR_SIZE);

        SDL_RenderPresent(renderer);

        SDL_Delay(max(0,16 - SDL_GetTicks() + lastTime)); //make sure the time interval is always the same
        printf("FPS: %d\n", (int)(1000.0f/(float)(SDL_GetTicks() - lastTime))); //print fps
    }

    if(renderer)
        SDL_DestroyRenderer(renderer);

    if(window)
        SDL_DestroyWindow(window);

    free(mapVectors);
    free(mapFaces);
    free(mapColours);
    SDL_DestroyWindow(window);
    SDL_Quit();

    return 0;
}

/*
map file format:
player.pos.x,player.pos.y,player.pos.z,player.vel.x,player.vel.y,player.vel.z,player.pitch,player.yaw,player.speed,player.accel,player.decel
num_of_vertices,num_of_faces,num_of_colours
vec0.x,vec0.y,vec0.z
vec1.x,vec1.y,vec1.z
vec2.x,vec2.y,vec2.z&(*player)
...
face0.p1,face0.p2,face0.p3,face0.texture,face0.norm.x,face0.norm.y,face0.norm.z,face0.type
face1.p1,face1.p2,face1.p3,face1.texture,face1.norm.x,face1.norm.y,face1.norm.z,face1.type
...
*/

/*
since calculating the surface normals for every face manually can be tedious, it is calculated by taking the cross product of the vectors p0-p1 and p0-p2
the type is used to determine whether the face normal points towards the origin or away (0 towards, 1 away)
*/

void loadMap(int *nVectors, int *nFaces, int *nColors, vec3 **vectors, face **faces, colour **colours, char *fileName, camera *player)
{
    FILE *mapFile = fopen(fileName, "r");
    fscanf(mapFile,"%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f\n", &(*player).pos.x, &(*player).pos.y, &(*player).pos.z, &(*player).vel.x, &(*player).vel.y, &(*player).vel.z, &(*player).pitch, &(*player).yaw, &(*player).speed, &(*player).accel, &(*player).decel);
    fscanf(mapFile,"%d,%d,%d\n",nVectors,nFaces,nColors);
    *vectors = (vec3 *)malloc(*nVectors * sizeof(vec3));
    *faces = (face *)malloc(*nFaces * sizeof(face));
    *colours = (colour *)malloc(*nColors * sizeof(colour));

    int i;
    for(i=0;i < *nVectors;i++)
        fscanf(mapFile,"%f,%f,%f\n",&(*vectors)[i].x, &(*vectors)[i].y, &(*vectors)[i].z);
    for(i=0;i < *nFaces;i++)
    {
        fscanf(mapFile,"%d,%d,%d,%d,%f,%f,%f,%d\n", &(*faces)[i].p1, &(*faces)[i].p2, &(*faces)[i].p3, &(*faces)[i].texture, &(*faces)[i].norm.x, &(*faces)[i].norm.y, &(*faces)[i].norm.z, &(*faces)[i].type);
        (*faces)[i].mid = mul(add((*vectors)[(*faces)[i].p1],add((*vectors)[(*faces)[i].p2],(*vectors)[(*faces)[i].p3])), 1.0/3.0);
        if(GENERATE_FACE_NORMALS)
        {
            (*faces)[i].norm = unit(cross(sub((*vectors)[(*faces)[i].p2], (*vectors)[(*faces)[i].p1]), sub((*vectors)[(*faces)[i].p3], (*vectors)[(*faces)[i].p1])));
            (*faces)[i].norm = mul((*faces)[i].norm, dot((*faces)[i].norm,(*faces)[i].mid) * ((*faces)[i].type * 2 - 1));
        }

    }
    for(i=0;i < *nColors;i++)
        fscanf(mapFile,"%d,%d,%d\n", &(*colours)[i].r, &(*colours)[i].g, &(*colours)[i].b);
    fclose(mapFile);
}

void loadConstants(int *w, int *h, float *sens, char *map, float *frustumW, float *frustumN, int *edges, char *fileName)
{
    FILE *settingsFile = fopen(fileName, "r");
    fscanf(settingsFile, "map = %[^\n]\n", map);
    fscanf(settingsFile, "width = %d\n", w);
    fscanf(settingsFile, "height = %d\n", h);
    fscanf(settingsFile, "sensitivity = %f\n", sens);
    fscanf(settingsFile, "frustum width = %f\n", frustumW);
    fscanf(settingsFile, "frustum near length = %f\n", frustumN);
    fscanf(settingsFile, "draw edges = %d", edges);
    fclose(settingsFile);
}

void drawFilledFaces(face *faces, int nFaces, camera player, vec3 *points, SDL_Renderer *renderer, colour *colours)
{
    int facesIndex[nFaces]; //index of each visible face
    int i, nVisible = 0;
    for(i=0;i < nFaces;i++) //cull faces which are facing away from the player, or are behind the player
        if((!BACKFACE_CULL_FILL || dot(sub(player.pos, points[faces[i].p1]), faces[i].norm) >= 0)
            && (rotateX(rotateZ(sub(points[faces[i].p1], player.pos), -player.yaw), -player.pitch).y >= FRUSTUM_NEAR_LENGTH
            || rotateX(rotateZ(sub(points[faces[i].p2], player.pos), -player.yaw), -player.pitch).y >= FRUSTUM_NEAR_LENGTH
            || rotateX(rotateZ(sub(points[faces[i].p3], player.pos), -player.yaw), -player.pitch).y >= FRUSTUM_NEAR_LENGTH))
            facesIndex[nVisible++] = i;

    if(nVisible > 0)
    {
        float dist[nFaces];//sort facesIndex based on median depth value of each corner of the triangle after translation and rotation relative to player
        for(i=0;i < nVisible;i++)
        {
            float L1 = length(rotateX(rotateZ(sub(points[faces[facesIndex[i]].p1], player.pos), -player.yaw), -player.pitch));
            float L2 = length(rotateX(rotateZ(sub(points[faces[facesIndex[i]].p2], player.pos), -player.yaw), -player.pitch));
            float L3 = length(rotateX(rotateZ(sub(points[faces[facesIndex[i]].p3], player.pos), -player.yaw), -player.pitch));
            dist[facesIndex[i]] = (L1 + L2 + L3);//min((L1 + L2 + L3 - min(min(L1, L2), L3) - max(max(L1, L2), L3)), (L1 + L2 + L3)/3.0);
        }

        quicksort(facesIndex, dist, 0, nVisible-1);

        //fill in order from furthest to closest (painter's algorithm) or front to back with some spicy clipping (reverse painter's algorithm)
        for(i=nVisible-1;i >= 0;i--)
        {
            SDL_SetRenderDrawColor(renderer, colours[faces[facesIndex[i]].texture].r, colours[faces[facesIndex[i]].texture].g, colours[faces[facesIndex[i]].texture].b, SDL_ALPHA_OPAQUE);
            transformFace(faces[facesIndex[i]], points, player, renderer);
        }

    }

}


void quicksort(int list[], float ref[], int l, int r)
{
    int a=l,b=r;
    int p = l;
    int hold;
    while(p != a || p != b)
    {
        while(ref[list[p]] <= ref[list[b]] && p != b)
            b--;
        hold = list[b];
        list[b] = list[p];
        list[p] = hold;
        p = b;
        while(ref[list[p]] >= ref[list[a]] && p != a)
            a++;
        hold = list[a];
        list[a] = list[p];
        list[p] = hold;
        p = a;
    }
    if(p > l)
        quicksort(list,ref,l,p-1);
    if(p < r)
        quicksort(list,ref,p+1,r);

}

void transformFace(face f, vec3 *points, camera player, SDL_Renderer *renderer) //also fills face
{
    vec3 pointsR[3] = {rotateX(rotateZ(sub(points[f.p1], player.pos), -player.yaw), -player.pitch), //rotate and translate points relative to player
    rotateX(rotateZ(sub(points[f.p2], player.pos), -player.yaw), -player.pitch),
    rotateX(rotateZ(sub(points[f.p3], player.pos), -player.yaw), -player.pitch)};
    vec3 forward = {0,1,0};

    int i;
    int nPoints = 0;
    vec2 pointsOut[4];
    for(i=0;i < 3;i++)
    {
        if(pointsR[i].y >= FRUSTUM_NEAR_LENGTH)
            pointsOut[nPoints++] = perspective2d(pointsR[i]);
        if((pointsR[i].y >= FRUSTUM_NEAR_LENGTH) != (pointsR[(i+1)%3].y >= FRUSTUM_NEAR_LENGTH))
            pointsOut[nPoints++] = perspective2d(add(pointsR[i], mul(sub(pointsR[(i+1)%3],pointsR[i]), dot(sub(mul(forward,FRUSTUM_NEAR_LENGTH),pointsR[i]),forward)/dot(sub(pointsR[(i+1)%3],pointsR[i]),forward))));
    }

    int codes[nPoints];
    for(i=0;i < nPoints;i++)
    {
        codes[i] = 0;
        if(pointsOut[i].y < 0)
            codes[i] |= 1;
        else if(pointsOut[i].y > HEIGHT)
            codes[i] |= 2;
        if(pointsOut[i].x > WIDTH)
            codes[i] |= 4;
        if(pointsOut[i].x < 0)
            codes[i] |= 8;
    }

    int visible = codes[0];
    for(i=1;i < nPoints;i++) //check if any segment of the polygon is in the view frustum
        visible &= codes[i];


    if(visible == 0)
    {
        fillTriangle(pointsOut[0], pointsOut[1], pointsOut[2], renderer);
        if(nPoints == 4)
        {
            fillTriangle(pointsOut[0], pointsOut[2], pointsOut[3], renderer);
            if(DRAW_EDGES == 2)
                SDL_SetRenderDrawColor(renderer, 50,50,50,SDL_ALPHA_OPAQUE);
            drawClippedLine(pointsOut[0], pointsOut[2], renderer);
            //SDL_RenderDrawLine(renderer, pointsOut[0].x + (float)WIDTH/2, pointsOut[0].y + (float)HEIGHT/2, pointsOut[2].x + (float)WIDTH/2, pointsOut[2].y + (float)HEIGHT/2);
        }

        if(DRAW_EDGES)
        {
            SDL_SetRenderDrawColor(renderer, 0,0,0,SDL_ALPHA_OPAQUE);
            drawWireframePolygon(pointsOut, nPoints, renderer);
        }
    }



}

void swapVec2Ptr(vec2 **p1, vec2 **p2)
{
    vec2 *hold = *p1;
    *p1 = *p2;
    *p2 = hold;
}

void fillTriangle(vec2 p1, vec2 p2, vec2 p3, SDL_Renderer *renderer)
{
    vec2 *top = &p1;
    vec2 *mid = &p2;
    vec2 *bot = &p3;

    /* //moved to the perspective2d function
    p1.x += (float)WIDTH/2; //shift to screen coords (width/2, height/2 is the origin instead of 0,0)
    p2.x += (float)WIDTH/2;
    p3.x += (float)WIDTH/2;

    p1.y += (float)HEIGHT/2;
    p2.y += (float)HEIGHT/2;
    p3.y += (float)HEIGHT/2;
    */

    //sort by y value

    if(top->y > bot->y)
        swapVec2Ptr(&top,&bot);
    if(top->y > mid->y)
        swapVec2Ptr(&top,&mid);
    if(mid->y > bot->y)
        swapVec2Ptr(&mid,&bot);

    vec2 mid2 = {.x = (bot->x - top->x) * (mid->y - top->y) / (bot->y - top->y) + top->x, .y = mid->y};

    if(mid->y != top->y) //draw flat bottom triangle
    {
        float starty = max(top->y, 0); //top
        float endy = min(mid->y, HEIGHT); //bottom

        float slope1 = (mid->x - top->x) / (mid->y - top->y);
        float slope2 = (mid2.x - top->x) / (mid2.y - top->y);

        float x1 = top->x + (slope1 * (starty - top->y));
        float x2 = top->x + (slope2 * (starty - top->y));

        float y;
        for(y = starty;y <= endy;y++)
        {
            if((x1 >= 0 || x2 >= 0) && (x1 <= WIDTH || x2 <= WIDTH))
                SDL_RenderDrawLine(renderer, clamp(min(x1,x2),0,WIDTH) - 1, y + 0.5f, clamp(max(x1,x2),0,WIDTH) + 1, y + 0.5f);
            x1 += slope1;
            x2 += slope2;
        }
    }

    if(mid->y != bot->y) //draw flat top triangle
    {
        float starty = max(mid->y, 0);
        float endy = min(bot->y, HEIGHT);

        float slope1 = (bot->x - mid->x) / (bot->y - mid->y);
        float slope2 = (bot->x - mid2.x) / (bot->y - mid2.y);

        float x1 = mid->x + (slope1 * (starty - mid->y));
        float x2 = mid2.x + (slope2 * (starty - mid->y));

        float y;
        for(y = starty;y <= endy;y++)
        {
            if((x1 >= 0 || x2 >= 0) && (x1 <= WIDTH || x2 <= WIDTH))
                SDL_RenderDrawLine(renderer, clamp(min(x1,x2),0,WIDTH), y + 0.5f, clamp(max(x1,x2),0,WIDTH) + 1, y + 0.5f);
            x1 += slope1;
            x2 += slope2;
        }
    }

    //SDL_RenderDrawLine(renderer, mid->x, mid->y, mid2.x, mid2.y);
    //SDL_RenderDrawLine(renderer, top->x , top->y + 0.5f, mid->x, mid->y + 0.5f);
    //SDL_RenderDrawLine(renderer, top->x, top->y + 0.5f, bot->x, bot->y + 0.5f);
    //SDL_RenderDrawLine(renderer, bot->x, bot->y + 0.5f, mid->x, mid->y + 0.5f);
    drawClippedLine(p1, p2, renderer);
    drawClippedLine(p1, p3, renderer);
    drawClippedLine(p3, p2, renderer);
}

void drawWireframePolygon(vec2 *polygon, int nPoints, SDL_Renderer *renderer)
{
    int i;
    for(i=0;i < nPoints;i++)
        drawClippedLine(polygon[i], polygon[(i+1)%nPoints], renderer);
        //SDL_RenderDrawLine(renderer, polygon[i].x + WIDTH/2, polygon[i].y + HEIGHT/2, polygon[(i+1)%nPoints].x + WIDTH/2, polygon[(i+1)%nPoints].y + HEIGHT/2);
}

int getClipCode(vec2 a) //up 1, down 2, right 4, left 8
{
    int out = 0;
    if(a.y < 0)
        out |= 1;
    else if(a.y > HEIGHT)
        out |= 2;
    if(a.x > WIDTH)
        out |= 4;
    else if(a.x < 0)
        out |= 8;
    return out;
}

void drawClippedLine(vec2 a, vec2 b, SDL_Renderer *renderer) //up 1, down 2, right 4, left 8
{
    bool found = false, valid = false;
    int codeA = getClipCode(a);
    int codeB = getClipCode(b);

    while(!found)
    {
        if(!(codeA | codeB))
            found = valid = true;
        else if((codeA & codeB))
            found = true;
        else
        {
            float x, y;
            int code = (codeA ? codeA : codeB);

            if(code & 1)
            {
                x = a.x + (b.x - a.x) * -a.y / (b.y - a.y);
                y = 0;
            }
            else if(code & 2)
            {
                x = a.x + (b.x - a.x) * (HEIGHT - a.y) / (b.y - a.y);
                y = HEIGHT;
            }
            else if(code & 4)
            {
                y = a.y + (b.y - a.y) * (WIDTH - a.x) / (b.x - a.x);
                x = WIDTH;
            }
            else if(code & 8)
            {
                y = a.y + (b.y - a.y) * -a.x / (b.x - a.x);
                x = 0;
            }

            if(code == codeA)
            {
                a.x = x;
                a.y = y;
                codeA = getClipCode(a);
            }
            else
            {
                b.x = x;
                b.y = y;
                codeB = getClipCode(b);
            }
        }
    }

    if(valid)
        SDL_RenderDrawLine(renderer, a.x, a.y + 0.5f, b.x, b.y + 0.5f);

}


//useless stuff

/*

frustum clipping rules:

each face is a triangle in 3d, and must be clipped to the view frustum
to generate clip + (slope1 * (starty - top->y));ped points, work with one point at a time
p2 is the point being used, p1 and p3 are the other 2 points
the maximum amount of points after clipping is 7, where all points are outside, all lines intersect the view frustum, and one corner is part of the resulting polygon

if p2 is inside the view frustum:
    always return p2
    if 2 intersections on different perpendicular planes (one corner) and opposite line (p1 to p3) does not intersect the view frustum, return the corner
    same test with parallel planes, return 2 corners

if p2 is outside the view frustum:
    if 1 intersection, return the intersection point
    if 2 intersections on the same plane, return both points
    if 2 intersections on perpendicular planes, return both points and corner


up = 0001
down = 0010
right = 0100
left = 1000

*/

/*
vec2* getClippedPolygon(face f, camera player, vec3 *points, int *nPoints)
{
    vec3 p1R = rotateX(rotateZ(sub(points[f.p1], player.pos), -player.yaw), -player.pitch); //rotate and translate points relative to player
    vec3 p2R = rotateX(rotateZ(sub(points[f.p2], player.pos), -player.yaw), -player.pitch);
    vec3 p3R = rotateX(rotateZ(sub(points[f.p3], player.pos), -player.yaw), -player.pitch);

    vec3 left = {-FRUSTUM_WIDTH, 1, 0}; //normals of the planes which make up the view frustum (point inwards)
    left = unit(left);
    vec3 right = {FRUSTUM_WIDTH, 1, 0};
    right = unit(right);
    vec3 up = {0, 1, FRUSTUM_WIDTH};
    up = unit(up);
    vec3 down = {0, 1, -FRUSTUM_WIDTH};
    down = unit(down);
    vec3 forward = {0,1,0};

    vec3 p12R = p1R, p13R = p1R;
    vec3 p21R = p2R, p23R = p2R;
    vec3 p31R = p3R, p32R = p3R;

    if(p1R.y < 0)
    {
        p12R = add(p1R, mul(sub(p2R,p1R), dot(mul(p1R, -1), forward)/dot(sub(p2R,p1R),forward)));
        p13R = add(p1R, mul(sub(p3R,p1R), dot(mul(p1R, -1), forward)/dot(sub(p3R,p1R),forward)));
    }

    if(p2R.y < 0)
    {
        p21R = add(p2R, mul(sub(p1R,p2R), dot(mul(p2R, -1), forward)/dot(sub(p1R,p2R),forward)));
        p23R = add(p2R, mul(sub(p3R,p2R), dot(mul(p2R, -1), forward)/dot(sub(p3R,p2R),forward)));
    }

    if(p3R.y < 0)
    {
        p31R = add(p3R, mul(sub(p1R,p3R), dot(mul(p3R, -1), forward)/dot(sub(p1R,p3R),forward)));
        p32R = add(p3R, mul(sub(p2R,p3R), dot(mul(p3R, -1), forward)/dot(sub(p2R,p3R),forward)));
    }

    int code12 = (dot(p12R, up) < 0) | ((dot(p12R, down) < 0) << 1) | ((dot(p12R, right) < 0) << 2) | ((dot(p12R, left) < 0) << 3);
    int code13 = (dot(p13R, up) < 0) | ((dot(p13R, down) < 0) << 1) | ((dot(p13R, right) < 0) << 2) | ((dot(p13R, left) < 0) << 3);
    int code21 = (dot(p21R, up) < 0) | ((dot(p21R, down) < 0) << 1) | ((dot(p21R, right) < 0) << 2) | ((dot(p21R, left) < 0) << 3);
    int code23 = (dot(p23R, up) < 0) | ((dot(p23R, down) < 0) << 1) | ((dot(p23R, right) < 0) << 2) | ((dot(p23R, left) < 0) << 3);
    int code31 = (dot(p31R, up) < 0) | ((dot(p31R, down) < 0) << 1) | ((dot(p31R, right) < 0) << 2) | ((dot(p31R, left) < 0) << 3);
    int code32 = (dot(p32R, up) < 0) | ((dot(p32R, down) < 0) << 1) | ((dot(p32R, right) < 0) << 2) | ((dot(p32R, left) < 0) << 3);

    if((code12 & code13 & code21 & code23 & code31 & code32) == 0)
    {
        if((code12 | code21) != 0)
        {
            if((code12 & 1) == 1)
                p12R = add(p12R, mul(sub(p21R,p12R), dot(mul(p12R,-1),up)/dot(sub(p21R,p12R),up)));
            else if((code12 & 2) == 2)
                p12R = add(p12R, mul(sub(p21R,p12R), dot(mul(p12R,-1),down)/dot(sub(p21R,p12R),down)));
            if((code12 & 4) == 4)
                p12R = add(p12R, mul(sub(p21R,p12R), dot(mul(p12R,-1),right)/dot(sub(p21R,p12R),right)));
            else if((code12 & 8) == 8)
                p12R = add(p12R, mul(sub(p21R,p12R), dot(mul(p12R,-1),left)/dot(sub(p21R,p12R),left)));

            if((code21 & 1) == 1)
                p21R = add(p12R, mul(sub(p21R,p12R), dot(mul(p12R,-1),up)/dot(sub(p21R,p12R),up)));
            else if((code21 & 2) == 2)
                p21R = add(p12R, mul(sub(p21R,p12R), dot(mul(p12R,-1),down)/dot(sub(p21R,p12R),down)));
            if((code21 & 4) == 4)
                p21R = add(p12R, mul(sub(p21R,p12R), dot(mul(p12R,-1),right)/dot(sub(p21R,p12R),right)));
            else if((code21 & 8) == 8)
                p21R = add(p12R, mul(sub(p21R,p12R), dot(mul(p12R,-1),left)/dot(sub(p21R,p12R),left)));
        }

        if((code13 | code31) != 0)
        {
            if((code13 & 1) == 1)
                p13R = add(p13R, mul(sub(p31R,p13R), dot(mul(p13R,-1),up)/dot(sub(p31R,p13R),up)));
            else if((code13 & 2) == 2)
                p13R = add(p13R, mul(sub(p31R,p13R), dot(mul(p13R,-1),down)/dot(sub(p31R,p13R),down)));
            if((code13 & 4) == 4)
                p13R = add(p13R, mul(sub(p31R,p13R), dot(mul(p13R,-1),right)/dot(sub(p31R,p13R),right)));
            else if((code13 & 8) == 8)
                p13R = add(p13R, mul(sub(p31R,p13R), dot(mul(p13R,-1),left)/dot(sub(p31R,p13R),left)));

            if((code31 & 1) == 1)
                p31R = add(p13R, mul(sub(p31R,p13R), dot(mul(p13R,-1),up)/dot(sub(p31R,p13R),up)));
            else if((code31 & 2) == 2)
                p31R = add(p13R, mul(sub(p31R,p13R), dot(mul(p13R,-1),down)/dot(sub(p31R,p13R),down)));
            if((code31 & 4) == 4)
                p31R = add(p13R, mul(sub(p31R,p13R), dot(mul(p13R,-1),right)/dot(sub(p31R,p13R),right)));
            else if((code31 & 8) == 8)
                p31R = add(p13R, mul(sub(p31R,p13R), dot(mul(p13R,-1),left)/dot(sub(p31R,p13R),left)));
        }

        if((code23 | code32) != 0)
        {
            if((code23 & 1) == 1)
                p23R = add(p23R, mul(sub(p32R,p23R), dot(mul(p23R,-1),up)/dot(sub(p32R,p23R),up)));
            else if((code23 & 2) == 2)
                p23R = add(p23R, mul(sub(p32R,p23R), dot(mul(p23R,-1),down)/dot(sub(p32R,p23R),down)));
            if((code23 & 4) == 4)
                p23R = add(p23R, mul(sub(p32R,p23R), dot(mul(p23R,-1),right)/dot(sub(p32R,p23R),right)));
            else if((code23 & 8) == 8)
                p23R = add(p23R, mul(sub(p32R,p23R), dot(mul(p23R,-1),left)/dot(sub(p32R,p23R),left)));

            if((code32 & 1) == 1)
                p32R = add(p23R, mul(sub(p32R,p23R), dot(mul(p23R,-1),up)/dot(sub(p32R,p23R),up)));
            else if((code32 & 2) == 2)
                p32R = add(p23R, mul(sub(p32R,p23R), dot(mul(p23R,-1),down)/dot(sub(p32R,p23R),down)));
            if((code32 & 4) == 4)
                p32R = add(p23R, mul(sub(p32R,p23R), dot(mul(p23R,-1),right)/dot(sub(p32R,p23R),right)));
            else if((code32 & 8) == 8)
                p32R = add(p23R, mul(sub(p32R,p23R), dot(mul(p23R,-1),left)/dot(sub(p32R,p23R),left)));
        }
    }

}
*/

void drawWireframeFace(face f, camera player, vec3 *points, SDL_Renderer *render)
{
    if(!BACKFACE_CULL_WIREFRAME || dot(sub(player.pos, points[f.p1]), f.norm) >= 0) //if backface culling is enabled, only draw if the face is facing towards the player
    {
        vec3 p1R = rotateX(rotateZ(sub(points[f.p1], player.pos), -player.yaw), -player.pitch); //rotate and translate points relative to player
        vec3 p2R = rotateX(rotateZ(sub(points[f.p2], player.pos), -player.yaw), -player.pitch);
        vec3 p3R = rotateX(rotateZ(sub(points[f.p3], player.pos), -player.yaw), -player.pitch);

        drawLine(p1R,p2R,render); //draw the 3 lines that make up the triangle face
        drawLine(p2R,p3R,render);
        drawLine(p3R,p1R,render);
    }
}

void drawLine(vec3 p1, vec3 p2, SDL_Renderer *render)
{
    vec3 start = {p1.x, p1.y, p1.z};
    vec3 end = {p2.x, p2.y, p2.z};
    vec3 disp = sub(end, start);

    if(start.y >= FRUSTUM_NEAR_LENGTH || end.y >= FRUSTUM_NEAR_LENGTH)
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


        if(start.y <= FRUSTUM_NEAR_LENGTH)
            start = add(start, mul(disp, dot(sub(mul(forward,FRUSTUM_NEAR_LENGTH),start),forward)/dot(disp,forward)));
            //start = add(start, mul(disp, dot(mul(start, -1), forward)/dot(disp,forward)));
        if(end.y <= FRUSTUM_NEAR_LENGTH)
            end = add(start, mul(disp, dot(sub(mul(forward,FRUSTUM_NEAR_LENGTH),start),forward)/dot(disp,forward)));
            //end = add(start, mul(disp, dot(mul(start, -1), forward)/dot(disp,forward)));

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
            /*
            if((code1 & 1) == 1) //start should be clipped to top plane
                start = add(start, mul(disp, dot(mul(start,-1),up)/dot(disp,up)));
            else if((code1 & 2) == 2) //start should be clipped to bottom plane
                start = add(start, mul(disp, dot(mul(start,-1),down)/dot(disp,down)));
            if((code1 & 4) == 4) //start should be clipped to right plane
                start = add(start, mul(disp, dot(mul(start,-1),right)/dot(disp,right)));
            else if((code1 & 8) == 8) //start should be clipped to left plane
                start = add(start, mul(disp, dot(mul(start,-1),left)/dot(disp,left)));


            if((code2 & 1) == 1) //end should be clipped to top plane
                end = add(start, mul(disp, dot(mul(start,-1),up)/dot(disp,up)));
            else if((code2 & 2) == 2) //end should be clipped to bottom plane
                end = add(start, mul(disp, dot(mul(start,-1),down)/dot(disp,down)));
            if((code2 & 4) == 4) //end should be clipped to right plane
                end = add(start, mul(disp, dot(mul(start,-1),right)/dot(disp,right)));
            else if((code2 & 8) == 8) //end should be clipped to left plane
                end = add(start, mul(disp, dot(mul(start,-1),left)/dot(disp,left)));
            */


            //3d projection transformation
            start.z *= FRUSTUM_WIDTH * (WIDTH/2) / start.y;
            start.x *= FRUSTUM_WIDTH * (WIDTH/2)  / start.y;

            end.z *= FRUSTUM_WIDTH * (WIDTH/2) / end.y;
            end.x *= FRUSTUM_WIDTH * (WIDTH/2)  / end.y;

            //printf("%f %f   %f %f", start.x, start.z, end.x, end.z);
            SDL_RenderDrawLine(render,start.x + WIDTH/2, start.z + HEIGHT/2, end.x + WIDTH/2, end.z + HEIGHT/2);
        }
    }

}

//more usefull stuff

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

vec2 add2(vec2 a, vec2 b)
{
    vec2 r = {.x = a.x + b.x, .y = a.y + b.y};
    return r;
}

vec2 sub2(vec2 a, vec2 b)
{
    vec2 r = {.x = a.x - b.x, .y = a.y - b.y};
    return r;
}

vec2 mul2(vec2 a, float b)
{
    vec2 r = {.x = a.x * b, .y = a.y * b};
    return r;
}

float length2(vec2 a)
{
    return sqrt((double)((a.x * a.x) + (a.y * a.y)));
}

float dot2(vec2 a, vec2 b)
{
    return (a.x * b.x) + (a.y * b.y);
}

float cross2(vec2 a, vec2 b) //magnitude of the cross product
{
    return (a.x * b.y) - (a.y * b.x);
}

vec2 unit2(vec2 a)
{
    float len = length2(a);
    vec2 r = mul2(a,(len != 0 ? 1/len : 0));//check for vector length of 0 to prevent divide by zero error
    return r;
}

vec3 toVec3(vec2 a, float z)
{
    vec3 r = {.x = a.x, .y = a.y, .z = z};
    return r;
}

vec2 perspective2d(vec3 a)
{
    vec2 r = {.x = a.x * FRUSTUM_WIDTH * ((float)WIDTH/2.0) / a.y + (float)WIDTH/2.0, .y = a.z * FRUSTUM_WIDTH * ((float)WIDTH/2.0) / a.y + (float)HEIGHT/2.0};
    return r;
}

bool intersection(vec2 a1, vec2 a2, vec2 b1, vec2 b2, vec2 *result)
{
    vec2 da = sub2(a1,a2);
    vec2 db = sub2(b1,b2);
    float ca = cross2(a1,a2);
    float cb = cross2(b1,b2);
    float base = cross2(da,db);
    if(base == 0)
        return false;
    *result = mul2(sub2(mul2(db,ca),mul2(da,cb)), 1.0/base);
    return true;
}

/*
void intersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float *xOut, float *yOut) //not using this anymore
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
*/
