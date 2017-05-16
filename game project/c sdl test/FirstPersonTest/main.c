#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <SDL.h>
#include <math.h>

#define WIDTH 800
#define HEIGHT 600
#define HFOV (WIDTH * 0.7f)
#define VFOV (HEIGHT * 0.7f)

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
    float angle, yaw, speed, accel, decel; //angle and yaw in radians (angle is pitch of the camera)
} camera;

float length(vec3 a);
float dot(vec3 a, vec3 b);

vec3 add(vec3 a, vec3 b);
vec3 sub(vec3 a, vec3 b);
vec3 mul(vec3 a, float b);
vec3 unit(vec3 a);
vec3 cross(vec3 a, vec3 b);
vec3 rotate(vec3 a, vec3 b, float theta);
vec3 rotateZ(vec3 a, float theta);
vec3 dropX(vec3 a);
vec3 dropY(vec3 a);
vec3 dropZ(vec3 a);

void intersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float *xOut, float *yOut);

int main(int argc, char **argv)
{
    SDL_Window *window = NULL;
    SDL_Renderer *renderer = NULL;

    if(SDL_Init(SDL_INIT_VIDEO) != 0)
        return 1;

    window = SDL_CreateWindow("Dank meme", SDL_WINDOWPOS_UNDEFINED, SDL_WINDOWPOS_UNDEFINED, WIDTH, HEIGHT, SDL_WINDOW_SHOWN);
    renderer = SDL_CreateRenderer(window,-1,SDL_RENDERER_ACCELERATED);
    //SDL_CreateWindowAndRenderer(WIDTH, HEIGHT, 0, &window, &renderer);

    camera player = {.pos = {.x = 0, .y = -500, .z = 0}, .vel = {.x = 0, .y = 0, .z = 0}, .angle = 0, .yaw = 0, .speed = 10, .accel = 1, .decel = 1.2};

    vec3 topLeft = {-400,200,-400};
    vec3 topRight = {400,200,-400};
    vec3 botLeft = {-400,200,400};
    vec3 botRight = {400,200,400};

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
        }

        //player movement & linear interpolation
        vec3 dv = {.x = (((wasd & 2) >> 1) - ((wasd & 8) >> 3)), .y = ((wasd & 1) - ((wasd & 4) >> 2)), .z = 0};
        dv = mul(unit(dv),player.accel);

        player.yaw += (((arrows & 8) >> 3) - ((arrows & 2) >> 1)) * 0.025;
        player.pos.z += (((arrows & 4) >> 2) - (arrows & 1)) * 5;

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
            player.vel = mul(unit(player.vel), player.speed);
        player.vel.z = zHold;

        player.pos = add(player.pos, player.vel); //move player based on velocity

        //printf("x: %0.2f y: %0.2f z: %0.2f  speed: %0.2f\n", player.pos.x, player.pos.y, player.pos.z, length(player.vel));

        SDL_SetRenderDrawColor(renderer, 255,255,255, SDL_ALPHA_OPAQUE);
        SDL_RenderClear(renderer);

        //stationary world, moveable player
        /*
        vec3 dirLine = {0,10,0};
        dirLine = rotateZ(dirLine, player.yaw);
        SDL_SetRenderDrawColor(renderer, 0,200,200, SDL_ALPHA_OPAQUE);
        SDL_RenderDrawLine(renderer, player.pos.x, player.pos.y, player.pos.x + dirLine.x, player.pos.y + dirLine.y);

        SDL_SetRenderDrawColor(renderer, 0,0,0, SDL_ALPHA_OPAQUE);
        SDL_RenderDrawPoint(renderer, player.pos.x, player.pos.y);
        */

        //stationary player, moveable world
        /*
        vec3 dirLine = {0,10,0};
        SDL_SetRenderDrawColor(renderer, 0,200,200, SDL_ALPHA_OPAQUE);
        SDL_RenderDrawLine(renderer, 400, 300, dirLine.x + 400, dirLine.y + 300); //shift everything right 400 and down 300 to center player
        SDL_RenderDrawLine(renderer, 400, 300, M_SQRT1_2*100 + 400, M_SQRT1_2*100 + 300);
        SDL_RenderDrawLine(renderer, 400, 300, -M_SQRT1_2*100 + 400, M_SQRT1_2*100 + 300);

        SDL_SetRenderDrawColor(renderer, 0,0,0, SDL_ALPHA_OPAQUE);
        SDL_RenderDrawPoint(renderer, 400, 300);

        vec3 topLeftR = rotateZ(sub(topLeft,player.pos), -player.yaw);
        vec3 topRightR = rotateZ(sub(topRight,player.pos), -player.yaw);
        vec3 botLeftR = rotateZ(sub(botLeft,player.pos), -player.yaw);
        vec3 botRightR = rotateZ(sub(botRight,player.pos), -player.yaw);

        SDL_SetRenderDrawColor(renderer, 200,0,200,SDL_ALPHA_OPAQUE);
        SDL_RenderDrawLine(renderer,topLeftR.x + 400, topLeftR.y + 300, topRightR.x + 400, topRightR.y + 300);
        SDL_RenderDrawLine(renderer,botLeftR.x + 400, botLeftR.y + 300, botRightR.x + 400, botRightR.y + 300); //skipping vertical lines because they wont be seen
        */

        //stationary player, moveable world, 3d projection //dont draw player


        vec3 topLeftR = rotateZ(sub(topLeft,player.pos), -player.yaw);
        vec3 topRightR = rotateZ(sub(topRight,player.pos), -player.yaw);
        vec3 botLeftR = rotateZ(sub(botLeft,player.pos), -player.yaw);
        vec3 botRightR = rotateZ(sub(botRight,player.pos), -player.yaw);


        //printf("%0.2f %0.2f %0.2f    ", topLeftR.x, topLeftR.y, topLeftR.z);

        /*
        if(topLeftR.y <= 0) //if point is behind player, find the intersection between the line and the x-axis
        {
            vec3 dir = unit(sub(topRightR, topLeftR));
            topLeftR = add(topLeftR,mul(dir,-topLeftR.y / dir.y));
            botLeftR = add(botLeftR,mul(dir,-botLeftR.y / dir.y));
            topLeftR.y += 0.001;
            botLeftR.y += 0.001;
        }

        if(topRightR.y <= 0)
        {
            vec3 dir = unit(sub(topLeftR, topRightR));
            topRightR = add(topRightR,mul(dir,-topRightR.y / dir.y));
            botRightR = add(botRightR,mul(dir,-botRightR.y / dir.y));
            topRightR.y += 0.001;
            botRightR.y += 0.001;
        }
        */
        if(topLeftR.y > 0 || topRightR.y > 0)
        {
            if(topLeftR.y <= 0 || topRightR.y <= 0)
            {
                float ix1, iy1, ix2, iy2; //find both intersection points (top down view)
                intersection(-0.0001,0.0001,-M_SQRT1_2,M_SQRT1_2,topLeftR.x,topLeftR.y,topRightR.x, topRightR.y, &ix1, &iy1); //left and forwards
                intersection(0.0001,0.0001,M_SQRT1_2,M_SQRT1_2,topLeftR.x,topLeftR.y,topRightR.x, topRightR.y, &ix2, &iy2); //right and forwards



                //printf("%0.2f %0.2f %0.2f   ", topLeftR.x, topLeftR.y, topLeftR.z);
                if(topLeftR.y < 0.0001)
                {
                    if(iy1 > 0)
                    {
                        topLeftR.x = ix1;
                        topLeftR.y = iy1;
                    }
                    else
                    {
                        topLeftR.x = ix2;
                        topLeftR.y = iy2;
                    }
                }

                if(topRightR.y < 0.0001)
                {
                    if(iy2 > 0)
                    {
                        topRightR.x = ix2;
                        topRightR.y = iy2;
                    }
                    else
                    {
                        topRightR.x = ix1;
                        topRightR.y = iy1;
                    }
                }

                botLeftR.x = topLeftR.x;
                botLeftR.y = topLeftR.y;

                botRightR.x = topRightR.x;
                botRightR.y = topRightR.y;
                //printf("%0.2f %0.2f %0.2f\n", topLeftR.x, topLeftR.y, topLeftR.z);
            }

            topLeftR.z *= VFOV / topLeftR.y;
            topRightR.z *= VFOV / topRightR.y;
            botLeftR.z *= VFOV / botLeftR.y;
            botRightR.z *= VFOV / botRightR.y;

            topLeftR.x *= -HFOV  / topLeftR.y;
            topRightR.x *= -HFOV / topRightR.y;
            botLeftR.x *= -HFOV / botLeftR.y;
            botRightR.x *= -HFOV / botRightR.y;

            //printf("%0.2f %0.2f %0.2f\n", topLeftR.x, topLeftR.y, topLeftR.z);
            SDL_SetRenderDrawColor(renderer, 200,0,200,SDL_ALPHA_OPAQUE);
            SDL_RenderDrawLine(renderer,topLeftR.x + 400, topLeftR.z + 300, topRightR.x + 400, topRightR.z + 300); //shift everything right 400 and down 300
            SDL_RenderDrawLine(renderer,botLeftR.x + 400, botLeftR.z + 300, botRightR.x + 400, botRightR.z + 300);
            SDL_RenderDrawLine(renderer,topLeftR.x + 400, topLeftR.z + 300, botLeftR.x + 400, botLeftR.z + 300);
            SDL_RenderDrawLine(renderer,topRightR.x + 400, topRightR.z + 300, botRightR.x + 400, botRightR.z + 300);
        }



        SDL_RenderPresent(renderer);
        SDL_Delay(16);
    }
    if (renderer)
        SDL_DestroyRenderer(renderer);

    if (window)
        SDL_DestroyWindow(window);

    SDL_DestroyWindow(window);
    SDL_Quit();

    return 0;
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

vec3 rotate(vec3 a, vec3 b, float theta) //rotates vector a theta radians around axis b
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
