extern printf
extern scanf

extern strcmp
extern strcat
extern strcpy


extern malloc
extern memcpy
extern free


struc map
.index RESD 1
.value RESD 1
.size:
endstruc



struc firstClass
.num RESD 1
.flag RESD 1
.size:
endstruc



struc point
.x RESD 1
.y RESD 1
.size:
endstruc



struc vector
.a RESB point.size
.b RESB point.size
.size:
endstruc



struc dvector
.v1 RESB vector.size
.v2 RESB vector.size
.size:
endstruc



section .text
global main

doNothing:
push ebp
mov ebp, esp
sub esp, 8
mov ecx, [ebp + 8]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
pusha
push 8
call malloc
add esp, 4
mov [ebp - 4], eax
popa
push dword [ebp + 8]
pop eax
push dword [ebp + 8]
pop eax
pusha
push 8
push eax
push dword [ebp - 4]
call memcpy
add esp, 12
popa
mov ecx, [ebp - 4]
push 1488
pop eax
mov ecx, [ebp - 4]
mov [ecx + firstClass.num], eax
mov ecx, [ebp + 8]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
mov ecx, [ebp - 4]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
mov esp, ebp
pop ebp
ret 4



recurse:
push ebp
mov ebp, esp
sub esp, 4
push dword [ebp + 8]
push 20
pop ebx
pop edx
cmp edx, ebx
jb  L51
mov eax, 0
jmp L52
L51:
mov eax, 1
L52:
push eax
pop eax
cmp eax, 1
jne L45
push dword [ebp + 8]
push 1
pop ebx
pop edx
add edx, ebx
mov eax, edx
push eax
call recurse
push eax
pop eax
mov [ebp + 8], eax
L45:
push dword [ebp + 8]
pop eax
mov esp, ebp
pop ebp
ret 4



getSqDistance:
push ebp
mov ebp, esp
sub esp, 8
mov ecx, [ebp + 8]
push dword [ecx + vector.a + point.x]
mov ecx, [ebp + 8]
push dword [ecx + vector.b + point.x]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
mov ecx, [ebp + 8]
push dword [ecx + vector.a + point.x]
mov ecx, [ebp + 8]
push dword [ecx + vector.b + point.x]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
pop ebx
pop eax
imul ebx
push eax
mov ecx, [ebp + 8]
push dword [ecx + vector.a + point.y]
mov ecx, [ebp + 8]
push dword [ecx + vector.b + point.y]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
mov ecx, [ebp + 8]
push dword [ecx + vector.a + point.y]
mov ecx, [ebp + 8]
push dword [ecx + vector.b + point.y]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
pop ebx
pop eax
imul ebx
push eax
pop ebx
pop edx
add edx, ebx
mov eax, edx
push eax
pop eax
mov [ebp - 4], eax
push tmp1
push str_format
call printf
add esp, 8
mov ecx, [ebp + 8]
push dword [ecx + vector.a + point.x]
mov ecx, [ebp + 8]
push dword [ecx + vector.b + point.x]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
push int_format
call printf
add esp, 8
push tmp2
push str_format
call printf
add esp, 8
mov ecx, [ebp + 8]
push dword [ecx + vector.a + point.y]
mov ecx, [ebp + 8]
push dword [ecx + vector.b + point.y]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
push int_format
call printf
add esp, 8
push tmp3
push str_format
call printf
add esp, 8
push dword [ebp - 4]
pop eax
mov esp, ebp
pop ebp
ret 4



getBean:
push ebp
mov ebp, esp
sub esp, 12
pusha
push 8
call malloc
add esp, 4
mov [ebp - 4], eax
popa
push 0
pop eax
mov ecx, [ebp - 4]
mov [ecx + firstClass.flag], eax
push 6
pop eax
mov ecx, [ebp - 4]
mov [ecx + firstClass.num], eax
mov ecx, [ebp - 4]
push dword [ecx + firstClass.flag]
push dword [globIntVar]
push 0
pop ebx
pop edx
cmp edx, ebx
je  L213
mov eax, 0
jmp L214
L213:
mov eax, 1
L214:
push eax
pop ebx
pop edx
cmp edx, 1
jne L201
cmp ebx, 1
jne L201
mov eax, 1
jmp L202
L201:
mov eax, 0
L202:
push eax
pop eax
mov [ebp - 8], eax
push dword [ebp - 8]
pop eax
cmp eax, 1
jne L225
mov ecx, [ebp - 4]
push dword [ecx + firstClass.flag]
push int_format
call printf
add esp, 8
jmp L226
L225:
mov ecx, [ebp - 4]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
L226:
push dword [ebp - 4]
push 8
call malloc
add esp, 4
pop ebx
push 8
push ebx
push eax
call memcpy
add esp, 12
push eax
pop eax
mov esp, ebp
pop ebp
ret



strread:
push ebp
mov ebp, esp
sub esp, 8
pusha
push 256
call malloc
add esp, 4
mov [ebp - 4], eax
push dword [ebp - 4]
push str_format
call scanf
add esp, 8
popa
push dword [ebp - 4]
push 256
call malloc
add esp, 4
pop ebx
push ebx
push eax
call strcpy
add esp, 8
push eax
pop eax
mov esp, ebp
pop ebp
ret



main:
push ebp
mov ebp, esp
sub esp, 84
push 5
pop eax
mov [globIntVar], eax
push tmp4
pop eax
pusha
push 256
call malloc
add esp, 4
mov [globalStr], eax
popa
pusha
push eax
push globalStr
call strcpy
add esp, 8
popa
pusha
push 8
call malloc
add esp, 4
mov [ebp - 4], eax
popa
push 1
pop eax
mov ecx, [ebp - 4]
mov [ecx + firstClass.flag], eax
push 23
pop eax
mov ecx, [ebp - 4]
mov [ecx + firstClass.num], eax
pusha
push 8
call malloc
add esp, 4
mov [motherfucker], eax
popa
push dword [ebp - 4]
pop eax
push dword [ebp - 4]
pop eax
pusha
push 8
push eax
push dword [motherfucker]
call memcpy
add esp, 12
popa
mov ecx, [motherfucker]
push tmp5
push str_format
call printf
add esp, 8
mov ecx, [motherfucker]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
push tmp6
push str_format
call printf
add esp, 8
push tmp7
pop eax
pusha
push 256
call malloc
add esp, 4
mov [ebp - 8], eax
popa
pusha
push eax
push dword [ebp - 8]
call strcpy
add esp, 8
popa
pusha
push 8
call malloc
add esp, 4
mov [ebp - 12], eax
popa
push dword [ebp - 8]
pop eax
mov ecx, [ebp - 12]
mov [ecx + map.value], eax
push 1
pop eax
mov ecx, [ebp - 12]
mov [ecx + map.index], eax
push tmp8
pop eax
pusha
push 256
call malloc
add esp, 4
mov [ebp - 16], eax
popa
pusha
push eax
push dword [ebp - 16]
call strcpy
add esp, 8
popa
mov ecx, [ebp - 12]
push dword [ecx + map.value]
push tmp9
push 256
call malloc
add esp, 4
pop ebx
pop edx
push edx
push eax
call strcpy
add esp, 8
push ebx
push eax
call strcat
add esp, 8
push eax
pop eax
pusha
push 256
call malloc
add esp, 4
mov [ebp - 20], eax
popa
pusha
push eax
push dword [ebp - 20]
call strcpy
add esp, 8
popa
push dword [ebp - 16]
pop eax
mov ecx, [ebp - 12]
pusha
push 256
call malloc
add esp, 4
mov [ecx + map.value], eax
popa
pusha
push eax
push dword [ecx + map.value]
call strcpy
add esp, 8
popa
push dword [ebp - 16]
push tmp10
push 256
call malloc
add esp, 4
pop ebx
pop edx
push edx
push eax
call strcpy
add esp, 8
push ebx
push eax
call strcat
add esp, 8
push eax
pop eax
pusha
push 256
call malloc
add esp, 4
mov [ebp - 16], eax
popa
pusha
push eax
push dword [ebp - 16]
call strcpy
add esp, 8
popa
push dword [ebp - 16]
push str_format
call printf
add esp, 8
mov ecx, [ebp - 12]
push dword [ecx + map.value]
push str_format
call printf
add esp, 8
pusha
push 8
call malloc
add esp, 4
mov [ebp - 24], eax
popa
call getBean
push eax
pop eax
call getBean
push eax
pop eax
pusha
push 8
push eax
push dword [ebp - 24]
call memcpy
add esp, 12
popa
mov ecx, [ebp - 24]
mov ecx, [ebp - 24]
push dword [ecx + firstClass.num]
push 6
pop ebx
pop edx
add edx, ebx
mov eax, edx
push eax
push int_format
call printf
add esp, 8
push tmp11
push str_format
call printf
add esp, 8
push globStringVarWithInit
push str_format
call printf
add esp, 8
pusha
push globStringVarWithInit
push str_format
call scanf
add esp, 8
popa
push globStringVarWithInit
push str_format
call printf
add esp, 8
call strread
push eax
pop eax
pusha
push 256
call malloc
add esp, 4
mov [ebp - 28], eax
popa
pusha
push eax
push dword [ebp - 28]
call strcpy
add esp, 8
popa
call strread
push eax
pop eax
pusha
push 256
call malloc
add esp, 4
mov [ebp - 32], eax
popa
pusha
push eax
push dword [ebp - 32]
call strcpy
add esp, 8
popa
call strread
push eax
pop eax
pusha
push 256
call malloc
add esp, 4
mov [ebp - 36], eax
popa
pusha
push eax
push dword [ebp - 36]
call strcpy
add esp, 8
popa
push dword [ebp - 24]
call doNothing
push tmp12
push str_format
call printf
add esp, 8
mov ecx, [ebp - 24]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
push tmp13
push str_format
call printf
add esp, 8
pusha
push 8
call malloc
add esp, 4
mov [ebp - 40], eax
popa
push 0
pop eax
mov ecx, [ebp - 40]
mov [ecx + point.y], eax
push 0
pop eax
mov ecx, [ebp - 40]
mov [ecx + point.x], eax
pusha
push 8
call malloc
add esp, 4
mov [ebp - 44], eax
popa
push 4
pop eax
mov ecx, [ebp - 44]
mov [ecx + point.y], eax
push 3
pop eax
mov ecx, [ebp - 44]
mov [ecx + point.x], eax
pusha
push 8
call malloc
add esp, 4
mov [ebp - 48], eax
popa
push 6
pop eax
mov ecx, [ebp - 48]
mov [ecx + point.y], eax
push 6
pop eax
mov ecx, [ebp - 48]
mov [ecx + point.x], eax
push tmp14
push str_format
call printf
add esp, 8
mov ecx, [ebp - 40]
push dword [ecx + point.y]
push int_format
call printf
add esp, 8
push tmp15
push str_format
call printf
add esp, 8
push tmp16
push str_format
call printf
add esp, 8
mov ecx, [ebp - 44]
push dword [ecx + point.y]
push int_format
call printf
add esp, 8
push tmp17
push str_format
call printf
add esp, 8
pusha
push 16
call malloc
add esp, 4
mov [ebp - 52], eax
popa
push dword [ebp - 40]
pop eax
mov ecx, [ebp - 52]
mov edx, [eax  + point.x]
mov [ecx + vector.b + point.x], edx
mov ecx, [ebp - 52]
mov edx, [eax  + point.y]
mov [ecx + vector.b + point.y], edx
push dword [ebp - 44]
pop eax
mov ecx, [ebp - 52]
mov edx, [eax  + point.x]
mov [ecx + vector.a + point.x], edx
mov ecx, [ebp - 52]
mov edx, [eax  + point.y]
mov [ecx + vector.a + point.y], edx
pusha
push 16
call malloc
add esp, 4
mov [ebp - 56], eax
popa
push dword [ebp - 44]
pop eax
mov ecx, [ebp - 56]
mov edx, [eax  + point.x]
mov [ecx + vector.b + point.x], edx
mov ecx, [ebp - 56]
mov edx, [eax  + point.y]
mov [ecx + vector.b + point.y], edx
push dword [ebp - 40]
pop eax
mov ecx, [ebp - 56]
mov edx, [eax  + point.x]
mov [ecx + vector.a + point.x], edx
mov ecx, [ebp - 56]
mov edx, [eax  + point.y]
mov [ecx + vector.a + point.y], edx
pusha
push 32
call malloc
add esp, 4
mov [ebp - 60], eax
popa
push dword [ebp - 56]
pop eax
mov ecx, [ebp - 60]
mov edx, [eax  + vector.a + point.x]
mov [ecx + dvector.v2 + vector.a + point.x], edx
mov ecx, [ebp - 60]
mov edx, [eax  + vector.a + point.y]
mov [ecx + dvector.v2 + vector.a + point.y], edx
mov ecx, [ebp - 60]
mov edx, [eax  + vector.b + point.x]
mov [ecx + dvector.v2 + vector.b + point.x], edx
mov ecx, [ebp - 60]
mov edx, [eax  + vector.b + point.y]
mov [ecx + dvector.v2 + vector.b + point.y], edx
push dword [ebp - 52]
pop eax
mov ecx, [ebp - 60]
mov edx, [eax  + vector.a + point.x]
mov [ecx + dvector.v1 + vector.a + point.x], edx
mov ecx, [ebp - 60]
mov edx, [eax  + vector.a + point.y]
mov [ecx + dvector.v1 + vector.a + point.y], edx
mov ecx, [ebp - 60]
mov edx, [eax  + vector.b + point.x]
mov [ecx + dvector.v1 + vector.b + point.x], edx
mov ecx, [ebp - 60]
mov edx, [eax  + vector.b + point.y]
mov [ecx + dvector.v1 + vector.b + point.y], edx
pusha
push 8
call malloc
add esp, 4
mov [ebp - 48], eax
popa
mov ecx, [ebp - 52]
push dword [ecx + vector.a]
pop eax
mov ecx, [ebp - 52]
push ecx
pop eax
mov ecx, [ebp - 48]
mov edx, [eax  + vector.a + point.x]
mov [ecx  + point.x], edx
mov ecx, [ebp - 48]
mov edx, [eax  + vector.a + point.y]
mov [ecx  + point.y], edx
mov ecx, [ebp - 48]
push dword [ebp - 48]
pop eax
mov ecx, [ebp - 60]
mov edx, [eax  + point.x]
mov [ecx + dvector.v2 + vector.b + point.x], edx
mov ecx, [ebp - 60]
mov edx, [eax  + point.y]
mov [ecx + dvector.v2 + vector.b + point.y], edx
push tmp18
push str_format
call printf
add esp, 8
mov ecx, [ebp - 48]
push dword [ecx + point.x]
push int_format
call printf
add esp, 8
push tmp19
push str_format
call printf
add esp, 8
push tmp20
push str_format
call printf
add esp, 8
mov ecx, [ebp - 48]
push dword [ecx + point.y]
push int_format
call printf
add esp, 8
push tmp21
push str_format
call printf
add esp, 8
push dword [ebp - 52]
call getSqDistance
push eax
pop eax
mov [ebp - 64], eax
push tmp22
push str_format
call printf
add esp, 8
push dword [ebp - 64]
push int_format
call printf
add esp, 8
push 2
pop eax
mov [ebp - 68], eax
push 10
pop eax
mov [ebp - 72], eax
push tmp23
push str_format
call printf
add esp, 8
pusha
lea ebx, [ebp - 72]
push ebx
push int_format
call scanf
add esp, 8
popa
push tmp24
push str_format
call printf
add esp, 8
pusha
lea ebx, [ebp - 68]
push ebx
push int_format
call scanf
add esp, 8
popa
push dword [ebp - 72]
push dword [ebp - 68]
call power
push eax
push int_format
call printf
add esp, 8
push tmp25
pop eax
pusha
push 256
call malloc
add esp, 4
mov [ebp - 76], eax
popa
pusha
push eax
push dword [ebp - 76]
call strcpy
add esp, 8
popa
push tmp26
pop eax
pusha
push 256
call malloc
add esp, 4
mov [ebp - 80], eax
popa
pusha
push eax
push dword [ebp - 80]
call strcpy
add esp, 8
popa
push dword [ebp - 76]
push dword [ebp - 80]
call strcmp
add esp, 8
mov ebx, eax
cmp ebx, 0
jne L855
mov eax, 0
jmp 856
L855:
mov eax, 1
L856:
push eax
pop eax
cmp eax, 1
jne L847
push dword [ebp - 76]
push dword [ebp - 80]
push 256
call malloc
add esp, 4
pop ebx
pop edx
push edx
push eax
call strcpy
add esp, 8
push ebx
push eax
call strcat
add esp, 8
push eax
push str_format
call printf
add esp, 8
L847:
push 0
pop eax
mov esp, ebp
pop ebp
ret



power:
push ebp
mov ebp, esp
sub esp, 8
push 1
pop eax
mov [ebp - 4], eax
push dword [ebp + 8]
push 0
pop ebx
pop edx
cmp edx, ebx
ja  L897
mov eax, 0
jmp L898
L897:
mov eax, 1
L898:
push eax
pop eax
cmp eax, 1
jne L891
L892:
push dword [ebp + 8]
push 2
pop ebx
pop eax
mov edx, 0
idiv ebx
mov eax, edx
push eax
push 1
pop ebx
pop edx
cmp edx, ebx
je  L913
mov eax, 0
jmp L914
L913:
mov eax, 1
L914:
push eax
pop eax
cmp eax, 1
jne L905
push dword [ebp - 4]
push dword [ebp + 12]
pop ebx
pop eax
imul ebx
push eax
pop eax
mov [ebp - 4], eax
L905:
push dword [ebp + 12]
push dword [ebp + 12]
pop ebx
pop eax
imul ebx
push eax
pop eax
mov [ebp + 12], eax
push dword [ebp + 8]
push 2
pop ebx
pop eax
mov edx, 0
idiv ebx
push eax
pop eax
mov [ebp + 8], eax
push dword [ebp + 8]
push 0
pop ebx
pop edx
cmp edx, ebx
ja  L953
mov eax, 0
jmp L954
L953:
mov eax, 1
L954:
push eax
pop eax
cmp eax, 1
je L892
L891:
push dword [ebp - 4]
pop eax
mov esp, ebp
pop ebp
ret 8



section .data
int_format dd "%d", 10, 0
str_format dd "%s", 10, 0
globIntVar: dd 0
globStringVarWithInit: dd "It is global string", 0
globBoolVar: dd 0
tmp1: dd " v.a.x - v.b.x = ", 0
tmp2: dd " v.a.y - v.b.y ", 0
tmp3: dd " ", 0
tmp4: dd "lol", 0
tmp5: dd "motherfucker.num = ", 0
tmp6: dd " ", 0
tmp7: dd "lolka", 0
tmp8: dd "parhatie", 0
tmp9: dd ", salam", 0
tmp10: dd ", fuckers!!!!!!!", 0
tmp11: dd " ", 0
tmp12: dd " ", 0
tmp13: dd " ", 0
tmp14: dd "x.y = ", 0
tmp15: dd " ", 0
tmp16: dd "y.y = ", 0
tmp17: dd " ", 0
tmp18: dd "zed.x = ", 0
tmp19: dd " ", 0
tmp20: dd "zed.y = ", 0
tmp21: dd " ", 0
tmp22: dd "dist = ", 0
tmp23: dd "Enter n: ", 0
tmp24: dd "Enter k: ", 0
tmp25: dd "  hello, ", 0
tmp26: dd "for all the world!  ", 0

section .bss
globalStr: resd 1
glob: resd 1
motherfucker: resb firstClass.size
